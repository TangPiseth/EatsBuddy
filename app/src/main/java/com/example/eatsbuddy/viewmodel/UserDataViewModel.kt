package com.example.eatsbuddy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatsbuddy.data.model.MealPreview
import com.example.eatsbuddy.ui.screens.MealTime
import com.example.eatsbuddy.ui.screens.PlannedMeal
import com.example.eatsbuddy.ui.screens.SimpleDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val TAG = "UserDataViewModel"

// Firebase data models
data class FirebaseGroceryItem(
    val id: String = "",
    val name: String = "",
    val quantity: String = "",
    val category: String = "OTHER",
    val isChecked: Boolean = false,
    val fromRecipe: String? = null
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", "OTHER", false, null)
    
    fun toGroceryItemData(): GroceryItemData {
        return GroceryItemData(
            id = id,
            name = name,
            quantity = quantity,
            category = try { GroceryCategoryType.valueOf(category) } catch (e: Exception) { GroceryCategoryType.OTHER },
            isChecked = isChecked,
            fromRecipe = fromRecipe
        )
    }
    
    companion object {
        fun fromGroceryItemData(item: GroceryItemData): FirebaseGroceryItem {
            return FirebaseGroceryItem(
                id = item.id,
                name = item.name,
                quantity = item.quantity,
                category = item.category.name,
                isChecked = item.isChecked,
                fromRecipe = item.fromRecipe
            )
        }
    }
}

data class FirebaseMealPlan(
    val id: String = "",
    val dateYear: Int = 0,
    val dateMonth: Int = 0,
    val dateDay: Int = 0,
    val mealTime: String = "BREAKFAST",
    val mealName: String = ""
) {
    // No-arg constructor for Firebase
    constructor() : this("", 0, 0, 0, "BREAKFAST", "")
    
    fun toPlannedMeal(): PlannedMeal {
        return PlannedMeal(
            id = id,
            date = SimpleDate(dateYear, dateMonth, dateDay),
            mealTime = try { MealTime.valueOf(mealTime) } catch (e: Exception) { MealTime.BREAKFAST },
            mealName = mealName
        )
    }
    
    companion object {
        fun fromPlannedMeal(meal: PlannedMeal): FirebaseMealPlan {
            return FirebaseMealPlan(
                id = meal.id,
                dateYear = meal.date.year,
                dateMonth = meal.date.month,
                dateDay = meal.date.day,
                mealTime = meal.mealTime.name,
                mealName = meal.mealName
            )
        }
    }
}

data class FirebaseFavorite(
    val mealId: String = "",
    val name: String = "",
    val thumbnailUrl: String? = null,
    val category: String? = null,
    val area: String? = null
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", null, null, null)
    
    fun toMealPreview(): MealPreview {
        return MealPreview(
            id = mealId,
            name = name,
            thumbnailUrl = thumbnailUrl,
            category = category,
            area = area,
            isFavorite = true
        )
    }
    
    companion object {
        fun fromMealPreview(meal: MealPreview): FirebaseFavorite {
            return FirebaseFavorite(
                mealId = meal.id,
                name = meal.name,
                thumbnailUrl = meal.thumbnailUrl,
                category = meal.category,
                area = meal.area
            )
        }
    }
}

data class UserDataState(
    val groceryItems: List<GroceryItemData> = emptyList(),
    val mealPlans: List<PlannedMeal> = emptyList(),
    val favorites: List<MealPreview> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null
)

class UserDataViewModel : ViewModel() {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    private val _state = MutableStateFlow(UserDataState())
    val state: StateFlow<UserDataState> = _state.asStateFlow()
    
    init {
        // Check if user is already logged in and load data immediately
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "User already logged in: ${currentUser.uid}, loading data...")
            loadAllUserData()
        }
        
        // Listen for auth state changes for future login/logout
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d(TAG, "Auth state changed: user = ${user?.uid}")
            if (user != null && _state.value.groceryItems.isEmpty() && 
                _state.value.favorites.isEmpty() && _state.value.mealPlans.isEmpty() &&
                !_state.value.isLoading) {
                // User logged in and we don't have data yet
                loadAllUserData()
            } else if (user == null) {
                // Clear data on logout
                _state.value = UserDataState()
            }
        }
    }
    
    private fun getCurrentUserId(): String? = auth.currentUser?.uid
    
    // ==================== LOAD ALL DATA ====================
    
    fun loadAllUserData() {
        val userId = getCurrentUserId() ?: return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            try {
                // Load all data in parallel
                val groceryItems = loadGroceryItemsFromFirebase(userId)
                val mealPlans = loadMealPlansFromFirebase(userId)
                val favorites = loadFavoritesFromFirebase(userId)
                
                _state.value = _state.value.copy(
                    groceryItems = groceryItems,
                    mealPlans = mealPlans,
                    favorites = favorites,
                    favoriteIds = favorites.map { it.id }.toSet(),
                    isLoading = false
                )
                
                Log.d(TAG, "Loaded all user data: ${groceryItems.size} grocery items, ${mealPlans.size} meal plans, ${favorites.size} favorites")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user data", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load data: ${e.message}"
                )
            }
        }
    }
    
    // ==================== GROCERY ITEMS ====================
    
    private suspend fun loadGroceryItemsFromFirebase(userId: String): List<GroceryItemData> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("groceryItems")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(FirebaseGroceryItem::class.java)?.toGroceryItemData()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading grocery items", e)
            emptyList()
        }
    }
    
    fun addGroceryItem(item: GroceryItemData) {
        val userId = getCurrentUserId()
        if (userId == null) {
            Log.w(TAG, "Cannot save grocery item - user not logged in")
            // Still add to local state for display
            val newItems = _state.value.groceryItems + item
            _state.value = _state.value.copy(groceryItems = newItems)
            return
        }
        
        Log.d(TAG, "Adding grocery item: ${item.name} for user: $userId")
        val newItems = _state.value.groceryItems + item
        _state.value = _state.value.copy(groceryItems = newItems)
        
        viewModelScope.launch {
            saveGroceryItemToFirebase(userId, item)
        }
    }
    
    fun addGroceryItems(items: List<GroceryItemData>) {
        val userId = getCurrentUserId()
        if (userId == null) {
            Log.w(TAG, "Cannot save grocery items - user not logged in")
            val newItems = _state.value.groceryItems + items
            _state.value = _state.value.copy(groceryItems = newItems)
            return
        }
        
        Log.d(TAG, "Adding ${items.size} grocery items for user: $userId")
        val newItems = _state.value.groceryItems + items
        _state.value = _state.value.copy(groceryItems = newItems)
        
        viewModelScope.launch {
            items.forEach { item ->
                saveGroceryItemToFirebase(userId, item)
            }
        }
    }
    
    fun updateGroceryItem(item: GroceryItemData) {
        val userId = getCurrentUserId() ?: return
        
        val updatedItems = _state.value.groceryItems.map { 
            if (it.id == item.id) item else it 
        }
        _state.value = _state.value.copy(groceryItems = updatedItems)
        
        viewModelScope.launch {
            saveGroceryItemToFirebase(userId, item)
        }
    }
    
    fun toggleGroceryItemChecked(itemId: String) {
        val userId = getCurrentUserId() ?: return
        
        val item = _state.value.groceryItems.find { it.id == itemId } ?: return
        val updatedItem = item.copy(isChecked = !item.isChecked)
        
        val updatedItems = _state.value.groceryItems.map { 
            if (it.id == itemId) updatedItem else it 
        }
        _state.value = _state.value.copy(groceryItems = updatedItems)
        
        viewModelScope.launch {
            saveGroceryItemToFirebase(userId, updatedItem)
        }
    }
    
    fun deleteGroceryItem(itemId: String) {
        val userId = getCurrentUserId() ?: return
        
        val updatedItems = _state.value.groceryItems.filter { it.id != itemId }
        _state.value = _state.value.copy(groceryItems = updatedItems)
        
        viewModelScope.launch {
            deleteGroceryItemFromFirebase(userId, itemId)
        }
    }
    
    fun clearCheckedGroceryItems() {
        val userId = getCurrentUserId() ?: return
        
        val checkedItems = _state.value.groceryItems.filter { it.isChecked }
        val remainingItems = _state.value.groceryItems.filter { !it.isChecked }
        _state.value = _state.value.copy(groceryItems = remainingItems)
        
        viewModelScope.launch {
            checkedItems.forEach { item ->
                deleteGroceryItemFromFirebase(userId, item.id)
            }
        }
    }
    
    fun clearAllGroceryItems() {
        val userId = getCurrentUserId() ?: return
        
        val allItems = _state.value.groceryItems
        _state.value = _state.value.copy(groceryItems = emptyList())
        
        viewModelScope.launch {
            allItems.forEach { item ->
                deleteGroceryItemFromFirebase(userId, item.id)
            }
        }
    }
    
    private suspend fun saveGroceryItemToFirebase(userId: String, item: GroceryItemData) {
        try {
            val firebaseItem = FirebaseGroceryItem.fromGroceryItemData(item)
            Log.d(TAG, "Saving grocery item to Firebase: users/$userId/groceryItems/${item.id}")
            firestore.collection("users")
                .document(userId)
                .collection("groceryItems")
                .document(item.id)
                .set(firebaseItem)
                .await()
            Log.d(TAG, "Successfully saved grocery item: ${item.name} to Firebase")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving grocery item to Firebase: ${e.message}", e)
        }
    }
    
    private suspend fun deleteGroceryItemFromFirebase(userId: String, itemId: String) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("groceryItems")
                .document(itemId)
                .delete()
                .await()
            Log.d(TAG, "Deleted grocery item: $itemId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery item", e)
        }
    }
    
    // ==================== MEAL PLANS ====================
    
    private suspend fun loadMealPlansFromFirebase(userId: String): List<PlannedMeal> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("mealPlans")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(FirebaseMealPlan::class.java)?.toPlannedMeal()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading meal plans", e)
            emptyList()
        }
    }
    
    fun addMealPlan(meal: PlannedMeal) {
        val userId = getCurrentUserId() ?: return
        
        val newMealPlans = _state.value.mealPlans + meal
        _state.value = _state.value.copy(mealPlans = newMealPlans)
        
        viewModelScope.launch {
            saveMealPlanToFirebase(userId, meal)
        }
    }
    
    fun deleteMealPlan(mealId: String) {
        val userId = getCurrentUserId() ?: return
        
        val updatedMealPlans = _state.value.mealPlans.filter { it.id != mealId }
        _state.value = _state.value.copy(mealPlans = updatedMealPlans)
        
        viewModelScope.launch {
            deleteMealPlanFromFirebase(userId, mealId)
        }
    }
    
    private suspend fun saveMealPlanToFirebase(userId: String, meal: PlannedMeal) {
        try {
            val firebaseMeal = FirebaseMealPlan.fromPlannedMeal(meal)
            firestore.collection("users")
                .document(userId)
                .collection("mealPlans")
                .document(meal.id)
                .set(firebaseMeal)
                .await()
            Log.d(TAG, "Saved meal plan: ${meal.mealName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving meal plan", e)
        }
    }
    
    private suspend fun deleteMealPlanFromFirebase(userId: String, mealId: String) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("mealPlans")
                .document(mealId)
                .delete()
                .await()
            Log.d(TAG, "Deleted meal plan: $mealId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting meal plan", e)
        }
    }
    
    // ==================== FAVORITES ====================
    
    private suspend fun loadFavoritesFromFirebase(userId: String): List<MealPreview> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(FirebaseFavorite::class.java)?.toMealPreview()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading favorites", e)
            emptyList()
        }
    }
    
    fun addFavorite(meal: MealPreview) {
        val userId = getCurrentUserId()
        
        // Don't add duplicates
        if (_state.value.favoriteIds.contains(meal.id)) {
            Log.d(TAG, "Favorite already exists: ${meal.id}")
            return
        }
        
        Log.d(TAG, "Adding favorite: ${meal.name} (${meal.id}) for user: $userId")
        val newFavorites = _state.value.favorites + meal.copy(isFavorite = true)
        val newFavoriteIds = _state.value.favoriteIds + meal.id
        _state.value = _state.value.copy(
            favorites = newFavorites,
            favoriteIds = newFavoriteIds
        )
        
        if (userId != null) {
            viewModelScope.launch {
                saveFavoriteToFirebase(userId, meal)
            }
        } else {
            Log.w(TAG, "Cannot save favorite to Firebase - user not logged in")
        }
    }
    
    fun removeFavorite(mealId: String) {
        val userId = getCurrentUserId()
        
        Log.d(TAG, "Removing favorite: $mealId for user: $userId")
        val updatedFavorites = _state.value.favorites.filter { it.id != mealId }
        val updatedFavoriteIds = _state.value.favoriteIds - mealId
        _state.value = _state.value.copy(
            favorites = updatedFavorites,
            favoriteIds = updatedFavoriteIds
        )
        
        if (userId != null) {
            viewModelScope.launch {
                deleteFavoriteFromFirebase(userId, mealId)
            }
        }
    }
    
    fun toggleFavorite(meal: MealPreview) {
        if (_state.value.favoriteIds.contains(meal.id)) {
            removeFavorite(meal.id)
        } else {
            addFavorite(meal)
        }
    }
    
    fun isFavorite(mealId: String): Boolean {
        return _state.value.favoriteIds.contains(mealId)
    }
    
    private suspend fun saveFavoriteToFirebase(userId: String, meal: MealPreview) {
        try {
            val firebaseFavorite = FirebaseFavorite.fromMealPreview(meal)
            Log.d(TAG, "Saving favorite to Firebase: users/$userId/favorites/${meal.id}")
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(meal.id)
                .set(firebaseFavorite)
                .await()
            Log.d(TAG, "Successfully saved favorite: ${meal.name} to Firebase")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving favorite to Firebase: ${e.message}", e)
        }
    }
    
    private suspend fun deleteFavoriteFromFirebase(userId: String, mealId: String) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(mealId)
                .delete()
                .await()
            Log.d(TAG, "Deleted favorite: $mealId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting favorite: ${e.message}", e)
        }
    }
    
    // ==================== UTILITY ====================
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    // Add ingredients from a recipe to grocery list
    fun addIngredientsFromRecipe(ingredients: List<String>, recipeName: String) {
        val newItems = ingredients.map { ingredientString ->
            val category = categorizeIngredient(ingredientString)
            GroceryItemData(
                id = UUID.randomUUID().toString(),
                name = ingredientString.trim(),
                quantity = "",
                category = category,
                fromRecipe = recipeName
            )
        }
        addGroceryItems(newItems)
    }
    
    // Smart categorization of ingredients (copied from GroceryViewModel)
    private fun categorizeIngredient(ingredient: String): GroceryCategoryType {
        val lower = ingredient.lowercase()
        
        return when {
            listOf("chicken", "beef", "pork", "lamb", "fish", "salmon", "shrimp", "tuna", 
                "bacon", "sausage", "ham", "turkey", "meat", "steak", "prawn", "crab", 
                "lobster", "cod", "tilapia").any { lower.contains(it) } -> GroceryCategoryType.MEAT_SEAFOOD
            
            listOf("milk", "cheese", "butter", "cream", "yogurt", "egg", "cheddar", 
                "parmesan", "mozzarella", "sour cream", "cottage", "ricotta", "feta").any { lower.contains(it) } -> GroceryCategoryType.DAIRY_EGGS
            
            listOf("onion", "garlic", "tomato", "potato", "carrot", "celery", "pepper", 
                "lettuce", "spinach", "broccoli", "mushroom", "cucumber", "zucchini", 
                "apple", "banana", "lemon", "lime", "orange", "berry", "avocado", 
                "cabbage", "corn", "pea", "bean", "ginger", "vegetable", "fruit",
                "parsley", "cilantro", "basil", "thyme", "rosemary", "mint", "chive").any { lower.contains(it) } -> GroceryCategoryType.FRUITS_VEGETABLES
            
            listOf("bread", "roll", "bun", "tortilla", "pita", "bagel", "croissant", 
                "muffin", "pastry").any { lower.contains(it) } -> GroceryCategoryType.BAKERY
            
            listOf("frozen", "ice cream", "ice").any { lower.contains(it) } -> GroceryCategoryType.FROZEN
            
            listOf("juice", "soda", "water", "coffee", "tea", "wine", "beer", 
                "drink", "beverage").any { lower.contains(it) } -> GroceryCategoryType.BEVERAGES
            
            listOf("chip", "cracker", "cookie", "candy", "chocolate", "snack", 
                "popcorn", "pretzel", "nut").any { lower.contains(it) } -> GroceryCategoryType.SNACKS
            
            listOf("flour", "sugar", "salt", "oil", "vinegar", "sauce", "pasta", 
                "rice", "noodle", "spice", "pepper", "cinnamon", "vanilla", "baking",
                "stock", "broth", "can", "canned", "dried", "honey", "syrup", "mustard",
                "ketchup", "mayo", "soy sauce", "worcestershire").any { lower.contains(it) } -> GroceryCategoryType.PANTRY
            
            else -> GroceryCategoryType.OTHER
        }
    }
}
