package com.example.eatsbuddy.data.model

import com.google.gson.annotations.SerializedName

// Response wrapper for meal search/lookup
data class MealResponse(
    @SerializedName("meals")
    val meals: List<MealDto>?
)

// Response wrapper for categories
data class CategoriesResponse(
    @SerializedName("categories")
    val categories: List<CategoryDto>?
)

// Response wrapper for filter results (simplified meal info)
data class FilterResponse(
    @SerializedName("meals")
    val meals: List<MealPreviewDto>?
)

// Response wrapper for list (categories/areas/ingredients)
data class ListResponse(
    @SerializedName("meals")
    val meals: List<ListItemDto>?
)

// Full meal details from API
data class MealDto(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strDrinkAlternate") val drinkAlternate: String?,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strArea") val area: String?,
    @SerializedName("strInstructions") val instructions: String?,
    @SerializedName("strMealThumb") val thumbnailUrl: String?,
    @SerializedName("strTags") val tags: String?,
    @SerializedName("strYoutube") val youtubeUrl: String?,
    @SerializedName("strSource") val sourceUrl: String?,
    // Ingredients (up to 20)
    @SerializedName("strIngredient1") val ingredient1: String?,
    @SerializedName("strIngredient2") val ingredient2: String?,
    @SerializedName("strIngredient3") val ingredient3: String?,
    @SerializedName("strIngredient4") val ingredient4: String?,
    @SerializedName("strIngredient5") val ingredient5: String?,
    @SerializedName("strIngredient6") val ingredient6: String?,
    @SerializedName("strIngredient7") val ingredient7: String?,
    @SerializedName("strIngredient8") val ingredient8: String?,
    @SerializedName("strIngredient9") val ingredient9: String?,
    @SerializedName("strIngredient10") val ingredient10: String?,
    @SerializedName("strIngredient11") val ingredient11: String?,
    @SerializedName("strIngredient12") val ingredient12: String?,
    @SerializedName("strIngredient13") val ingredient13: String?,
    @SerializedName("strIngredient14") val ingredient14: String?,
    @SerializedName("strIngredient15") val ingredient15: String?,
    @SerializedName("strIngredient16") val ingredient16: String?,
    @SerializedName("strIngredient17") val ingredient17: String?,
    @SerializedName("strIngredient18") val ingredient18: String?,
    @SerializedName("strIngredient19") val ingredient19: String?,
    @SerializedName("strIngredient20") val ingredient20: String?,
    // Measures (up to 20)
    @SerializedName("strMeasure1") val measure1: String?,
    @SerializedName("strMeasure2") val measure2: String?,
    @SerializedName("strMeasure3") val measure3: String?,
    @SerializedName("strMeasure4") val measure4: String?,
    @SerializedName("strMeasure5") val measure5: String?,
    @SerializedName("strMeasure6") val measure6: String?,
    @SerializedName("strMeasure7") val measure7: String?,
    @SerializedName("strMeasure8") val measure8: String?,
    @SerializedName("strMeasure9") val measure9: String?,
    @SerializedName("strMeasure10") val measure10: String?,
    @SerializedName("strMeasure11") val measure11: String?,
    @SerializedName("strMeasure12") val measure12: String?,
    @SerializedName("strMeasure13") val measure13: String?,
    @SerializedName("strMeasure14") val measure14: String?,
    @SerializedName("strMeasure15") val measure15: String?,
    @SerializedName("strMeasure16") val measure16: String?,
    @SerializedName("strMeasure17") val measure17: String?,
    @SerializedName("strMeasure18") val measure18: String?,
    @SerializedName("strMeasure19") val measure19: String?,
    @SerializedName("strMeasure20") val measure20: String?
) {
    // Helper function to get all ingredients as a list
    fun getIngredientsList(): List<Ingredient> {
        val ingredients = listOf(
            ingredient1 to measure1,
            ingredient2 to measure2,
            ingredient3 to measure3,
            ingredient4 to measure4,
            ingredient5 to measure5,
            ingredient6 to measure6,
            ingredient7 to measure7,
            ingredient8 to measure8,
            ingredient9 to measure9,
            ingredient10 to measure10,
            ingredient11 to measure11,
            ingredient12 to measure12,
            ingredient13 to measure13,
            ingredient14 to measure14,
            ingredient15 to measure15,
            ingredient16 to measure16,
            ingredient17 to measure17,
            ingredient18 to measure18,
            ingredient19 to measure19,
            ingredient20 to measure20
        )
        
        return ingredients
            .filter { (ingredient, _) -> !ingredient.isNullOrBlank() }
            .map { (ingredient, measure) -> 
                Ingredient(
                    name = ingredient!!.trim(),
                    measure = measure?.trim() ?: ""
                )
            }
    }
    
    // Helper function to get instructions as a list of steps
    fun getInstructionsList(): List<String> {
        return instructions
            ?.split("\r\n", "\n")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }
}

// Simplified meal info from filter endpoints
data class MealPreviewDto(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumbnailUrl: String?
)

// Category details
data class CategoryDto(
    @SerializedName("idCategory") val id: String,
    @SerializedName("strCategory") val name: String,
    @SerializedName("strCategoryThumb") val thumbnailUrl: String?,
    @SerializedName("strCategoryDescription") val description: String?
)

// List item (for category/area/ingredient lists)
data class ListItemDto(
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strArea") val area: String?,
    @SerializedName("strIngredient") val ingredient: String?
)

// Domain models for the app
data class Ingredient(
    val name: String,
    val measure: String
) {
    override fun toString(): String {
        return if (measure.isNotBlank()) "$measure $name" else name
    }
}

data class Meal(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: List<String>,
    val thumbnailUrl: String?,
    val tags: List<String>,
    val youtubeUrl: String?,
    val sourceUrl: String?,
    val ingredients: List<Ingredient>,
    val isFavorite: Boolean = false
)

data class MealPreview(
    val id: String,
    val name: String,
    val thumbnailUrl: String?,
    val category: String? = null,
    val area: String? = null,
    val isFavorite: Boolean = false
)

data class Category(
    val id: String,
    val name: String,
    val thumbnailUrl: String?,
    val description: String?
)

// Extension functions to convert DTOs to domain models
fun MealDto.toMeal(): Meal {
    return Meal(
        id = id,
        name = name,
        category = category ?: "Unknown",
        area = area ?: "Unknown",
        instructions = getInstructionsList(),
        thumbnailUrl = thumbnailUrl,
        tags = tags?.split(",")?.map { it.trim() } ?: emptyList(),
        youtubeUrl = youtubeUrl,
        sourceUrl = sourceUrl,
        ingredients = getIngredientsList()
    )
}

fun MealPreviewDto.toMealPreview(): MealPreview {
    return MealPreview(
        id = id,
        name = name,
        thumbnailUrl = thumbnailUrl
    )
}

fun CategoryDto.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        thumbnailUrl = thumbnailUrl,
        description = description
    )
}
