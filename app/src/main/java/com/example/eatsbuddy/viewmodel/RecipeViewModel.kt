package com.example.eatsbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatsbuddy.data.model.Category
import com.example.eatsbuddy.data.model.Meal
import com.example.eatsbuddy.data.model.MealPreview
import com.example.eatsbuddy.data.repository.MealRepository
import com.example.eatsbuddy.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RecipesUiState(
    val meals: List<MealPreview> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RecipeDetailUiState(
    val meal: Meal? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class HomeUiState(
    val popularMeals: List<MealPreview> = emptyList(),
    val categories: List<Category> = emptyList(),
    val randomMeal: Meal? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class FavoritesUiState(
    val favoriteMeals: List<MealPreview> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeViewModel : ViewModel() {
    
    private val repository = MealRepository()
    
    // Recipes list state
    private val _recipesState = MutableStateFlow(RecipesUiState())
    val recipesState: StateFlow<RecipesUiState> = _recipesState.asStateFlow()
    
    // Recipe detail state
    private val _detailState = MutableStateFlow(RecipeDetailUiState())
    val detailState: StateFlow<RecipeDetailUiState> = _detailState.asStateFlow()
    
    // Home page state
    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()
    
    // Favorites state
    private val _favoritesState = MutableStateFlow(FavoritesUiState())
    val favoritesState: StateFlow<FavoritesUiState> = _favoritesState.asStateFlow()
    
    // Local storage of favorite meals (full data for display)
    private val favoriteMealsCache = mutableMapOf<String, MealPreview>()
    
    init {
        loadCategories()
        loadHomeData()
    }
    
    // Load categories for filter
    fun loadCategories() {
        viewModelScope.launch {
            when (val result = repository.getCategories()) {
                is Result.Success -> {
                    _recipesState.value = _recipesState.value.copy(
                        categories = result.data
                    )
                    _homeState.value = _homeState.value.copy(
                        categories = result.data
                    )
                }
                is Result.Error -> {
                    // Categories failed, but continue without them
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Load home page data
    fun loadHomeData() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true)
            
            // Load random meals for "Popular" section
            when (val result = repository.getRandomMeals(6)) {
                is Result.Success -> {
                    _homeState.value = _homeState.value.copy(
                        popularMeals = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _homeState.value = _homeState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Search meals by name
    fun searchMeals(query: String) {
        _recipesState.value = _recipesState.value.copy(
            searchQuery = query,
            selectedCategory = null
        )
        
        if (query.isBlank()) {
            _recipesState.value = _recipesState.value.copy(
                meals = emptyList(),
                isLoading = false
            )
            return
        }
        
        viewModelScope.launch {
            _recipesState.value = _recipesState.value.copy(isLoading = true, error = null)
            
            when (val result = repository.searchMeals(query)) {
                is Result.Success -> {
                    _recipesState.value = _recipesState.value.copy(
                        meals = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _recipesState.value = _recipesState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Filter meals by category
    fun filterByCategory(category: String?) {
        _recipesState.value = _recipesState.value.copy(
            selectedCategory = category,
            searchQuery = ""
        )
        
        if (category == null) {
            _recipesState.value = _recipesState.value.copy(
                meals = emptyList(),
                isLoading = false
            )
            return
        }
        
        viewModelScope.launch {
            _recipesState.value = _recipesState.value.copy(isLoading = true, error = null)
            
            when (val result = repository.getMealsByCategory(category)) {
                is Result.Success -> {
                    _recipesState.value = _recipesState.value.copy(
                        meals = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _recipesState.value = _recipesState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Load meal details
    fun loadMealDetails(mealId: String) {
        viewModelScope.launch {
            _detailState.value = RecipeDetailUiState(isLoading = true)
            
            when (val result = repository.getMealById(mealId)) {
                is Result.Success -> {
                    _detailState.value = RecipeDetailUiState(
                        meal = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _detailState.value = RecipeDetailUiState(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Toggle favorite
    fun toggleFavorite(mealId: String) {
        val isFavorite = repository.toggleFavorite(mealId)
        
        // If adding to favorites, cache the meal data
        if (isFavorite) {
            // Try to get from detail state first
            _detailState.value.meal?.let { meal ->
                if (meal.id == mealId) {
                    favoriteMealsCache[mealId] = MealPreview(
                        id = meal.id,
                        name = meal.name,
                        thumbnailUrl = meal.thumbnailUrl,
                        category = meal.category,
                        area = meal.area,
                        isFavorite = true
                    )
                }
            }
            // Or from recipes list
            _recipesState.value.meals.find { it.id == mealId }?.let { meal ->
                favoriteMealsCache[mealId] = meal.copy(isFavorite = true)
            }
            // Or from home popular meals
            _homeState.value.popularMeals.find { it.id == mealId }?.let { meal ->
                favoriteMealsCache[mealId] = meal.copy(isFavorite = true)
            }
        } else {
            favoriteMealsCache.remove(mealId)
        }
        
        // Update meals list
        _recipesState.value = _recipesState.value.copy(
            meals = _recipesState.value.meals.map { meal ->
                if (meal.id == mealId) meal.copy(isFavorite = isFavorite) else meal
            }
        )
        
        // Update home state
        _homeState.value = _homeState.value.copy(
            popularMeals = _homeState.value.popularMeals.map { meal ->
                if (meal.id == mealId) meal.copy(isFavorite = isFavorite) else meal
            }
        )
        
        // Update detail state if viewing this meal
        _detailState.value.meal?.let { meal ->
            if (meal.id == mealId) {
                _detailState.value = _detailState.value.copy(
                    meal = meal.copy(isFavorite = isFavorite)
                )
            }
        }
        
        // Update favorites state
        updateFavoritesState()
    }
    
    // Update favorites state from cache
    private fun updateFavoritesState() {
        _favoritesState.value = _favoritesState.value.copy(
            favoriteMeals = favoriteMealsCache.values.toList()
        )
    }
    
    // Get all favorites
    fun getFavorites(): List<MealPreview> {
        return favoriteMealsCache.values.toList()
    }
    
    // Check if a meal is favorite
    fun isFavorite(mealId: String): Boolean {
        return repository.isFavorite(mealId)
    }
    
    // Clear error
    fun clearError() {
        _recipesState.value = _recipesState.value.copy(error = null)
        _detailState.value = _detailState.value.copy(error = null)
        _homeState.value = _homeState.value.copy(error = null)
    }
    
    // Load initial recipes (by default category)
    fun loadInitialRecipes() {
        if (_recipesState.value.meals.isEmpty() && 
            _recipesState.value.searchQuery.isBlank() && 
            _recipesState.value.selectedCategory == null) {
            // Load some default recipes - using "Chicken" category as default
            filterByCategory("Chicken")
        }
    }
}
