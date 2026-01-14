package com.example.eatsbuddy.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

// Data class for grocery item
data class GroceryItemData(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: String = "",
    val category: GroceryCategoryType = GroceryCategoryType.OTHER,
    val isChecked: Boolean = false,
    val fromRecipe: String? = null // Track which recipe this came from
)

// Categories for grocery items
enum class GroceryCategoryType(
    val displayName: String,
    val emoji: String
) {
    FRUITS_VEGETABLES("Fruits & Veggies", "🥬"),
    MEAT_SEAFOOD("Meat & Seafood", "🥩"),
    DAIRY_EGGS("Dairy & Eggs", "🥛"),
    BAKERY("Bakery", "🍞"),
    PANTRY("Pantry", "🥫"),
    FROZEN("Frozen", "🧊"),
    BEVERAGES("Beverages", "🥤"),
    SNACKS("Snacks", "🍿"),
    OTHER("Other", "📦")
}

data class GroceryUiState(
    val items: List<GroceryItemData> = emptyList(),
    val selectedCategory: GroceryCategoryType? = null
)

class GroceryViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(GroceryUiState())
    val uiState: StateFlow<GroceryUiState> = _uiState.asStateFlow()
    
    // Add a single item
    fun addItem(name: String, quantity: String = "", category: GroceryCategoryType = GroceryCategoryType.OTHER) {
        if (name.isBlank()) return
        
        val newItem = GroceryItemData(
            name = name.trim(),
            quantity = quantity,
            category = category
        )
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items + newItem
        )
    }
    
    // Add ingredients from a recipe
    fun addIngredientsFromRecipe(ingredients: List<String>, recipeName: String) {
        val newItems = ingredients.map { ingredientString ->
            // Parse the ingredient string (e.g., "2 cups Flour" -> quantity="2 cups", name="Flour")
            val parts = ingredientString.trim()
            val category = categorizeIngredient(parts)
            
            GroceryItemData(
                name = parts,
                quantity = "",
                category = category,
                fromRecipe = recipeName
            )
        }
        
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items + newItems
        )
    }
    
    // Smart categorization of ingredients
    private fun categorizeIngredient(ingredient: String): GroceryCategoryType {
        val lower = ingredient.lowercase()
        
        return when {
            // Meat & Seafood
            listOf("chicken", "beef", "pork", "lamb", "fish", "salmon", "shrimp", "tuna", 
                "bacon", "sausage", "ham", "turkey", "meat", "steak", "prawn", "crab", 
                "lobster", "cod", "tilapia").any { lower.contains(it) } -> GroceryCategoryType.MEAT_SEAFOOD
            
            // Dairy & Eggs
            listOf("milk", "cheese", "butter", "cream", "yogurt", "egg", "cheddar", 
                "parmesan", "mozzarella", "sour cream", "cottage", "ricotta", "feta").any { lower.contains(it) } -> GroceryCategoryType.DAIRY_EGGS
            
            // Fruits & Vegetables
            listOf("onion", "garlic", "tomato", "potato", "carrot", "celery", "pepper", 
                "lettuce", "spinach", "broccoli", "mushroom", "cucumber", "zucchini", 
                "apple", "banana", "lemon", "lime", "orange", "berry", "avocado", 
                "cabbage", "corn", "pea", "bean", "ginger", "vegetable", "fruit",
                "parsley", "cilantro", "basil", "thyme", "rosemary", "mint", "chive").any { lower.contains(it) } -> GroceryCategoryType.FRUITS_VEGETABLES
            
            // Bakery
            listOf("bread", "roll", "bun", "tortilla", "pita", "bagel", "croissant", 
                "muffin", "pastry").any { lower.contains(it) } -> GroceryCategoryType.BAKERY
            
            // Frozen
            listOf("frozen", "ice cream", "ice").any { lower.contains(it) } -> GroceryCategoryType.FROZEN
            
            // Beverages
            listOf("juice", "soda", "water", "coffee", "tea", "wine", "beer", 
                "drink", "beverage").any { lower.contains(it) } -> GroceryCategoryType.BEVERAGES
            
            // Snacks
            listOf("chip", "cracker", "cookie", "candy", "chocolate", "snack", 
                "popcorn", "pretzel", "nut").any { lower.contains(it) } -> GroceryCategoryType.SNACKS
            
            // Pantry (default for most cooking ingredients)
            listOf("flour", "sugar", "salt", "oil", "vinegar", "sauce", "pasta", 
                "rice", "noodle", "spice", "pepper", "cinnamon", "vanilla", "baking",
                "stock", "broth", "can", "canned", "dried", "honey", "syrup", "mustard",
                "ketchup", "mayo", "soy sauce", "worcestershire").any { lower.contains(it) } -> GroceryCategoryType.PANTRY
            
            else -> GroceryCategoryType.OTHER
        }
    }
    
    // Toggle item checked state
    fun toggleItemChecked(itemId: String) {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map { item ->
                if (item.id == itemId) item.copy(isChecked = !item.isChecked) else item
            }
        )
    }
    
    // Delete item
    fun deleteItem(itemId: String) {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filter { it.id != itemId }
        )
    }
    
    // Clear checked items
    fun clearCheckedItems() {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filter { !it.isChecked }
        )
    }
    
    // Clear all items
    fun clearAllItems() {
        _uiState.value = _uiState.value.copy(items = emptyList())
    }
    
    // Set category filter
    fun setSelectedCategory(category: GroceryCategoryType?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
    
    // Get filtered items
    fun getFilteredItems(): List<GroceryItemData> {
        val state = _uiState.value
        return if (state.selectedCategory != null) {
            state.items.filter { it.category == state.selectedCategory }
        } else {
            state.items
        }
    }
    
    // Get progress
    fun getProgress(): Pair<Int, Int> {
        val items = _uiState.value.items
        return Pair(items.count { it.isChecked }, items.size)
    }
}
