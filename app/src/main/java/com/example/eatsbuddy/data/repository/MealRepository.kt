package com.example.eatsbuddy.data.repository

import com.example.eatsbuddy.data.api.RetrofitInstance
import com.example.eatsbuddy.data.model.Category
import com.example.eatsbuddy.data.model.Meal
import com.example.eatsbuddy.data.model.MealPreview
import com.example.eatsbuddy.data.model.toCategory
import com.example.eatsbuddy.data.model.toMeal
import com.example.eatsbuddy.data.model.toMealPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class MealRepository {
    
    private val api = RetrofitInstance.mealDbApi
    
    // Store favorite meal IDs locally (in a real app, use DataStore or Room)
    private val favoriteMealIds = mutableSetOf<String>()
    
    suspend fun searchMeals(query: String): Result<List<MealPreview>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchMealsByName(query)
                val meals = response.meals?.map { meal ->
                    MealPreview(
                        id = meal.id,
                        name = meal.name,
                        thumbnailUrl = meal.thumbnailUrl,
                        category = meal.category,
                        area = meal.area,
                        isFavorite = favoriteMealIds.contains(meal.id)
                    )
                } ?: emptyList()
                Result.Success(meals)
            } catch (e: Exception) {
                Result.Error("Failed to search meals: ${e.message}", e)
            }
        }
    }
    
    suspend fun getMealById(id: String): Result<Meal> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMealById(id)
                val meal = response.meals?.firstOrNull()?.toMeal()
                    ?.copy(isFavorite = favoriteMealIds.contains(id))
                if (meal != null) {
                    Result.Success(meal)
                } else {
                    Result.Error("Meal not found")
                }
            } catch (e: Exception) {
                Result.Error("Failed to get meal: ${e.message}", e)
            }
        }
    }
    
    suspend fun getRandomMeal(): Result<Meal> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRandomMeal()
                val meal = response.meals?.firstOrNull()?.toMeal()
                if (meal != null) {
                    Result.Success(meal.copy(isFavorite = favoriteMealIds.contains(meal.id)))
                } else {
                    Result.Error("No random meal found")
                }
            } catch (e: Exception) {
                Result.Error("Failed to get random meal: ${e.message}", e)
            }
        }
    }
    
    suspend fun getCategories(): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCategories()
                val categories = response.categories?.map { it.toCategory() } ?: emptyList()
                Result.Success(categories)
            } catch (e: Exception) {
                Result.Error("Failed to get categories: ${e.message}", e)
            }
        }
    }
    
    suspend fun getMealsByCategory(category: String): Result<List<MealPreview>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.filterByCategory(category)
                val meals = response.meals?.map { meal ->
                    meal.toMealPreview().copy(
                        category = category,
                        isFavorite = favoriteMealIds.contains(meal.id)
                    )
                } ?: emptyList()
                Result.Success(meals)
            } catch (e: Exception) {
                Result.Error("Failed to get meals by category: ${e.message}", e)
            }
        }
    }
    
    suspend fun getMealsByArea(area: String): Result<List<MealPreview>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.filterByArea(area)
                val meals = response.meals?.map { meal ->
                    meal.toMealPreview().copy(
                        area = area,
                        isFavorite = favoriteMealIds.contains(meal.id)
                    )
                } ?: emptyList()
                Result.Success(meals)
            } catch (e: Exception) {
                Result.Error("Failed to get meals by area: ${e.message}", e)
            }
        }
    }
    
    suspend fun getMealsByIngredient(ingredient: String): Result<List<MealPreview>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.filterByIngredient(ingredient)
                val meals = response.meals?.map { meal ->
                    meal.toMealPreview().copy(
                        isFavorite = favoriteMealIds.contains(meal.id)
                    )
                } ?: emptyList()
                Result.Success(meals)
            } catch (e: Exception) {
                Result.Error("Failed to get meals by ingredient: ${e.message}", e)
            }
        }
    }
    
    suspend fun getAreas(): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAreaList()
                val areas = response.meals?.mapNotNull { it.area } ?: emptyList()
                Result.Success(areas)
            } catch (e: Exception) {
                Result.Error("Failed to get areas: ${e.message}", e)
            }
        }
    }
    
    // Get multiple random meals for homepage
    suspend fun getRandomMeals(count: Int): Result<List<MealPreview>> {
        return withContext(Dispatchers.IO) {
            try {
                val meals = mutableListOf<MealPreview>()
                val seenIds = mutableSetOf<String>()
                
                repeat(count) {
                    try {
                        val response = api.getRandomMeal()
                        response.meals?.firstOrNull()?.let { meal ->
                            if (!seenIds.contains(meal.id)) {
                                seenIds.add(meal.id)
                                meals.add(
                                    MealPreview(
                                        id = meal.id,
                                        name = meal.name,
                                        thumbnailUrl = meal.thumbnailUrl,
                                        category = meal.category,
                                        area = meal.area,
                                        isFavorite = favoriteMealIds.contains(meal.id)
                                    )
                                )
                            }
                        }
                    } catch (e: Exception) {
                        // Ignore individual failures
                    }
                }
                
                Result.Success(meals)
            } catch (e: Exception) {
                Result.Error("Failed to get random meals: ${e.message}", e)
            }
        }
    }
    
    // Toggle favorite status
    fun toggleFavorite(mealId: String): Boolean {
        return if (favoriteMealIds.contains(mealId)) {
            favoriteMealIds.remove(mealId)
            false
        } else {
            favoriteMealIds.add(mealId)
            true
        }
    }
    
    fun isFavorite(mealId: String): Boolean {
        return favoriteMealIds.contains(mealId)
    }
    
    fun getFavoriteIds(): Set<String> {
        return favoriteMealIds.toSet()
    }
}
