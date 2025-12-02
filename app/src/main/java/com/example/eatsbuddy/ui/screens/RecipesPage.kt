package com.example.eatsbuddy.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.components.BottomNavigationBar
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange

// Full Recipe data class
data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val rating: Float,
    val reviewCount: Int,
    val prepTime: String,
    val cookTime: String,
    val servings: Int,
    val calories: Int,
    val difficulty: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val isFavorite: Boolean = false,
    val imageEmoji: String = "🍽️"
)

// Filter/Sort options
enum class RecipeSortOption(val displayName: String) {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    RATING_HIGH("Rating (High to Low)"),
    RATING_LOW("Rating (Low to High)"),
    PREP_TIME_SHORT("Prep Time (Shortest)"),
    PREP_TIME_LONG("Prep Time (Longest)")
}

enum class RecipeFilterCategory(val displayName: String, val emoji: String) {
    ALL("All", "📋"),
    BREAKFAST("Breakfast", "🍳"),
    LUNCH("Lunch", "🥗"),
    DINNER("Dinner", "🍝"),
    DESSERT("Dessert", "🍰"),
    SNACKS("Snacks", "🍿"),
    DRINKS("Drinks", "🥤"),
    FAVORITES("Favorites", "❤️")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesPage(
    onBackClick: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {},
    initialCategory: String? = null,
    currentRoute: String = "recipes",
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { 
        mutableStateOf(
            if (initialCategory != null) {
                RecipeFilterCategory.entries.find { it.displayName == initialCategory } 
                    ?: RecipeFilterCategory.ALL
            } else {
                RecipeFilterCategory.ALL
            }
        )
    }
    var selectedSort by remember { mutableStateOf(RecipeSortOption.RATING_HIGH) }
    var showSortDropdown by remember { mutableStateOf(false) }
    
    // Sample recipes data
    var recipes by remember {
        mutableStateOf(
            listOf(
                Recipe(
                    id = 1,
                    name = "Spaghetti Carbonara",
                    description = "Classic Italian pasta with creamy egg sauce and crispy bacon",
                    category = "Dinner",
                    rating = 4.8f,
                    reviewCount = 256,
                    prepTime = "15 min",
                    cookTime = "15 min",
                    servings = 4,
                    calories = 450,
                    difficulty = "Medium",
                    ingredients = listOf("400g spaghetti", "200g bacon", "4 eggs", "100g parmesan", "Black pepper"),
                    instructions = listOf("Boil pasta", "Fry bacon", "Mix eggs with cheese", "Combine all"),
                    isFavorite = true,
                    imageEmoji = "🍝"
                ),
                Recipe(
                    id = 2,
                    name = "Chicken Stir Fry",
                    description = "Quick and healthy Asian-style chicken with vegetables",
                    category = "Dinner",
                    rating = 4.5f,
                    reviewCount = 189,
                    prepTime = "10 min",
                    cookTime = "15 min",
                    servings = 3,
                    calories = 320,
                    difficulty = "Easy",
                    ingredients = listOf("500g chicken breast", "Mixed vegetables", "Soy sauce", "Ginger", "Garlic"),
                    instructions = listOf("Cut chicken", "Stir fry vegetables", "Add chicken", "Season"),
                    isFavorite = false,
                    imageEmoji = "🍗"
                ),
                Recipe(
                    id = 3,
                    name = "Greek Salad",
                    description = "Fresh Mediterranean salad with feta cheese and olives",
                    category = "Lunch",
                    rating = 4.6f,
                    reviewCount = 145,
                    prepTime = "15 min",
                    cookTime = "0 min",
                    servings = 2,
                    calories = 180,
                    difficulty = "Easy",
                    ingredients = listOf("Tomatoes", "Cucumber", "Red onion", "Feta cheese", "Olives", "Olive oil"),
                    instructions = listOf("Chop vegetables", "Add feta and olives", "Drizzle with olive oil"),
                    isFavorite = true,
                    imageEmoji = "🥗"
                ),
                Recipe(
                    id = 4,
                    name = "Banana Pancakes",
                    description = "Fluffy pancakes made with ripe bananas and a hint of cinnamon",
                    category = "Breakfast",
                    rating = 4.7f,
                    reviewCount = 312,
                    prepTime = "10 min",
                    cookTime = "10 min",
                    servings = 2,
                    calories = 280,
                    difficulty = "Easy",
                    ingredients = listOf("2 ripe bananas", "2 eggs", "1 cup flour", "Milk", "Cinnamon"),
                    instructions = listOf("Mash bananas", "Mix ingredients", "Cook on griddle"),
                    isFavorite = false,
                    imageEmoji = "🥞"
                ),
                Recipe(
                    id = 5,
                    name = "Chocolate Lava Cake",
                    description = "Decadent chocolate cake with a molten center",
                    category = "Dessert",
                    rating = 4.9f,
                    reviewCount = 423,
                    prepTime = "15 min",
                    cookTime = "12 min",
                    servings = 4,
                    calories = 380,
                    difficulty = "Medium",
                    ingredients = listOf("Dark chocolate", "Butter", "Eggs", "Sugar", "Flour"),
                    instructions = listOf("Melt chocolate", "Mix batter", "Bake until edges set"),
                    isFavorite = true,
                    imageEmoji = "🍫"
                ),
                Recipe(
                    id = 6,
                    name = "Avocado Toast",
                    description = "Simple and nutritious breakfast with creamy avocado",
                    category = "Breakfast",
                    rating = 4.3f,
                    reviewCount = 178,
                    prepTime = "5 min",
                    cookTime = "2 min",
                    servings = 1,
                    calories = 220,
                    difficulty = "Easy",
                    ingredients = listOf("Bread", "Avocado", "Salt", "Pepper", "Lemon juice"),
                    instructions = listOf("Toast bread", "Mash avocado", "Season and serve"),
                    isFavorite = false,
                    imageEmoji = "🥑"
                ),
                Recipe(
                    id = 7,
                    name = "Mango Smoothie",
                    description = "Refreshing tropical smoothie perfect for summer",
                    category = "Drinks",
                    rating = 4.4f,
                    reviewCount = 98,
                    prepTime = "5 min",
                    cookTime = "0 min",
                    servings = 2,
                    calories = 150,
                    difficulty = "Easy",
                    ingredients = listOf("2 mangoes", "1 cup yogurt", "Honey", "Ice"),
                    instructions = listOf("Blend all ingredients until smooth"),
                    isFavorite = false,
                    imageEmoji = "🥤"
                ),
                Recipe(
                    id = 8,
                    name = "Crispy Popcorn",
                    description = "Movie night essential with your choice of seasoning",
                    category = "Snacks",
                    rating = 4.2f,
                    reviewCount = 67,
                    prepTime = "2 min",
                    cookTime = "5 min",
                    servings = 4,
                    calories = 120,
                    difficulty = "Easy",
                    ingredients = listOf("Popcorn kernels", "Oil", "Salt", "Butter"),
                    instructions = listOf("Heat oil", "Add kernels", "Season when popped"),
                    isFavorite = false,
                    imageEmoji = "🍿"
                )
            )
        )
    }

    // Filter and sort recipes
    val filteredRecipes = recipes
        .filter { recipe ->
            val matchesSearch = searchQuery.isEmpty() || 
                recipe.name.contains(searchQuery, ignoreCase = true) ||
                recipe.description.contains(searchQuery, ignoreCase = true)
            
            val matchesCategory = when (selectedCategory) {
                RecipeFilterCategory.ALL -> true
                RecipeFilterCategory.FAVORITES -> recipe.isFavorite
                else -> recipe.category == selectedCategory.displayName
            }
            
            matchesSearch && matchesCategory
        }
        .sortedWith { a, b ->
            when (selectedSort) {
                RecipeSortOption.NAME_ASC -> a.name.compareTo(b.name)
                RecipeSortOption.NAME_DESC -> b.name.compareTo(a.name)
                RecipeSortOption.RATING_HIGH -> b.rating.compareTo(a.rating)
                RecipeSortOption.RATING_LOW -> a.rating.compareTo(b.rating)
                RecipeSortOption.PREP_TIME_SHORT -> extractMinutes(a.prepTime).compareTo(extractMinutes(b.prepTime))
                RecipeSortOption.PREP_TIME_LONG -> extractMinutes(b.prepTime).compareTo(extractMinutes(a.prepTime))
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recipes 📖",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search recipes...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(RecipeFilterCategory.entries.toList()) { category ->
                        RecipeFilterChip(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sort Dropdown & Results Count
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredRecipes.size} recipes found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Box {
                        Card(
                            modifier = Modifier.clickable { showSortDropdown = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedSort.displayName,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Sort",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        
                        DropdownMenu(
                            expanded = showSortDropdown,
                            onDismissRequest = { showSortDropdown = false }
                        ) {
                            RecipeSortOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.displayName) },
                                    onClick = {
                                        selectedSort = option
                                        showSortDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Recipe List
            if (filteredRecipes.isEmpty()) {
                item {
                    EmptyRecipeState(searchQuery = searchQuery)
                }
            } else {
                items(filteredRecipes, key = { it.id }) { recipe ->
                    RecipeListItem(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe.id) },
                        onFavoriteClick = {
                            recipes = recipes.map {
                                if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite) else it
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun RecipeFilterChip(
    category: RecipeFilterCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) GreenPrimary else GreenPrimary.copy(alpha = 0.1f),
        label = "chipBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
        label = "chipText"
    )

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = category.emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Recipe Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenLight.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = recipe.imageEmoji, fontSize = 40.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Recipe Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (recipe.isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Recipe Meta Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Orange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${recipe.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Prep Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⏱️", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = recipe.prepTime,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Category Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GreenPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = recipe.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = GreenPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyRecipeState(
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🔍", fontSize = 50.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (searchQuery.isNotEmpty()) "No recipes found" else "No recipes in this category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (searchQuery.isNotEmpty()) 
                    "Try searching for something else" 
                else 
                    "Try selecting a different category",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Helper function to extract minutes from prep time string
private fun extractMinutes(prepTime: String): Int {
    return prepTime.filter { it.isDigit() }.toIntOrNull() ?: 0
}

@Preview(showBackground = true)
@Composable
fun RecipesPagePreview() {
    EatsBuddyTheme {
        RecipesPage()
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListItemPreview() {
    EatsBuddyTheme {
        RecipeListItem(
            recipe = Recipe(
                id = 1,
                name = "Spaghetti Carbonara",
                description = "Classic Italian pasta with creamy egg sauce",
                category = "Dinner",
                rating = 4.8f,
                reviewCount = 256,
                prepTime = "15 min",
                cookTime = "15 min",
                servings = 4,
                calories = 450,
                difficulty = "Medium",
                ingredients = listOf(),
                instructions = listOf(),
                isFavorite = true,
                imageEmoji = "🍝"
            ),
            onClick = {},
            onFavoriteClick = {}
        )
    }
}
