package com.example.eatsbuddy.data.api

import com.example.eatsbuddy.data.model.CategoriesResponse
import com.example.eatsbuddy.data.model.FilterResponse
import com.example.eatsbuddy.data.model.ListResponse
import com.example.eatsbuddy.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApi {
    
    companion object {
        const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }
    
    // Search meal by name
    @GET("search.php")
    suspend fun searchMealsByName(
        @Query("s") name: String
    ): MealResponse
    
    // List all meals by first letter
    @GET("search.php")
    suspend fun searchMealsByFirstLetter(
        @Query("f") letter: Char
    ): MealResponse
    
    // Lookup full meal details by id
    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealResponse
    
    // Lookup a single random meal
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
    
    // List all meal categories with details
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse
    
    // List all category names
    @GET("list.php?c=list")
    suspend fun getCategoryList(): ListResponse
    
    // List all areas
    @GET("list.php?a=list")
    suspend fun getAreaList(): ListResponse
    
    // List all ingredients
    @GET("list.php?i=list")
    suspend fun getIngredientList(): ListResponse
    
    // Filter by main ingredient
    @GET("filter.php")
    suspend fun filterByIngredient(
        @Query("i") ingredient: String
    ): FilterResponse
    
    // Filter by category
    @GET("filter.php")
    suspend fun filterByCategory(
        @Query("c") category: String
    ): FilterResponse
    
    // Filter by area/cuisine
    @GET("filter.php")
    suspend fun filterByArea(
        @Query("a") area: String
    ): FilterResponse
}
