package com.example.eatsbuddy.ui.screens

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.eatsbuddy.ui.components.BottomNavigationBar
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eatsbuddy.data.model.Category
import com.example.eatsbuddy.data.model.MealPreview
import com.example.eatsbuddy.data.model.UserProfile
import com.example.eatsbuddy.ui.components.LoadingIndicator
import com.example.eatsbuddy.ui.components.SearchBar
import com.example.eatsbuddy.ui.components.SectionHeader
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange

// Data class for quick access features
data class QuickAccessFeature(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val backgroundColor: Color
)

// Data class for recipe categories
data class RecipeCategory(
    val name: String,
    val emoji: String,
    val color: Color
)

// Data class for recipe preview
data class RecipePreview(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Float,
    val prepTime: String,
    val isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    userProfile: UserProfile? = null,
    isAuthenticated: Boolean = false,
    popularMeals: List<MealPreview> = emptyList(),
    categories: List<Category> = emptyList(),
    isLoadingMeals: Boolean = false,
    onProfileClick: () -> Unit = {},
    onRecipesClick: () -> Unit = {},
    onMealPlannerClick: () -> Unit = {},
    onGroceryListClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onRecipeClick: (String) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onFavoriteClick: (String) -> Unit = {},
    currentRoute: String = "home",
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    // Quick access features for the 3 main pages
    val quickAccessFeatures = remember {
        listOf(
            QuickAccessFeature(
                title = "Recipes",
                subtitle = "Browse & Save",
                icon = Icons.Default.Search,
                backgroundColor = GreenPrimary
            ),
            QuickAccessFeature(
                title = "Meal Planner",
                subtitle = "Plan Your Week",
                icon = Icons.Default.DateRange,
                backgroundColor = Orange
            ),
            QuickAccessFeature(
                title = "Grocery List",
                subtitle = "Shop Smart",
                icon = Icons.Default.ShoppingCart,
                backgroundColor = GreenLight
            )
        )
    }

    // Color palette for categories
    val categoryColors = remember {
        listOf(
            Color(0xFFFFB74D), // Orange
            Color(0xFF81C784), // Green
            Color(0xFF64B5F6), // Blue
            Color(0xFFF48FB1), // Pink
            Color(0xFFFFD54F), // Yellow
            Color(0xFF4DD0E1), // Cyan
            Color(0xFF9575CD), // Purple
            Color(0xFFE57373), // Red
            Color(0xFFAED581), // Light Green
            Color(0xFF4FC3F7), // Light Blue
            Color(0xFFFFCC80), // Light Orange
            Color(0xFFCE93D8), // Light Purple
            Color(0xFF80DEEA), // Teal
            Color(0xFFF06292)  // Pink
        )
    }

    // Get emoji for category based on name
    fun getCategoryEmoji(categoryName: String): String {
        return when (categoryName.lowercase()) {
            "beef" -> "🥩"
            "chicken" -> "🍗"
            "dessert" -> "🍰"
            "lamb" -> "🍖"
            "miscellaneous" -> "🍳"
            "pasta" -> "🍝"
            "pork" -> "🥓"
            "seafood" -> "🦐"
            "side" -> "🥗"
            "starter" -> "🥟"
            "vegan" -> "🥬"
            "vegetarian" -> "🥕"
            "breakfast" -> "🍳"
            "goat" -> "🐐"
            else -> "🍽️"
        }
    }

    // Shuffle categories once when they change
    val shuffledCategories = remember(categories) {
        categories.shuffled().take(8) // Take first 8 shuffled categories
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🍽️",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EatsBuddy",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    // Profile avatar
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isAuthenticated && userProfile != null) {
                            if (userProfile.profilePictureUrl != null) {
                                AsyncImage(
                                    model = userProfile.profilePictureUrl,
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = getInitials(userProfile.firstName, userProfile.lastName),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            }
                        } else {
                            Text(text = "👤", fontSize = 20.sp)
                        }
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
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Welcome Banner with Search
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp, bottom = 24.dp)
                ) {
                    Column {
                        Text(
                            text = if (isAuthenticated && userProfile != null) 
                                "Welcome, ${userProfile.firstName}! 👋" 
                            else 
                                "Welcome Back! 👋",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "What would you like to cook today?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it }
                        )
                    }
                }
            }

            // Quick Access Cards - Main Features
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(title = "Quick Access")
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        quickAccessFeatures.forEachIndexed { index, feature ->
                            QuickAccessCard(
                                feature = feature,
                                onClick = {
                                    when (index) {
                                        0 -> onRecipesClick()
                                        1 -> onMealPlannerClick()
                                        2 -> onGroceryListClick()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recipe Categories from API
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "Categories 🍴",
                        actionText = "See All",
                        onActionClick = onRecipesClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                if (shuffledCategories.isEmpty()) {
                    // Loading or empty state
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(6) { index ->
                            // Placeholder chips
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.Gray.copy(alpha = 0.2f))
                            )
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(
                            count = shuffledCategories.size,
                            key = { shuffledCategories[it].id }
                        ) { index ->
                            val category = shuffledCategories[index]
                            val color = categoryColors[index % categoryColors.size]
                            val emoji = getCategoryEmoji(category.name)
                            
                            ApiCategoryChip(
                                name = category.name,
                                emoji = emoji,
                                color = color,
                                onClick = { onCategoryClick(category.name) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Popular Recipes from API
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "Popular Recipes 🔥",
                        actionText = "See All",
                        onActionClick = onRecipesClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                if (isLoadingMeals) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(message = "Loading recipes...")
                    }
                } else if (popularMeals.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No recipes available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(popularMeals, key = { it.id }) { meal ->
                            PopularMealCard(
                                meal = meal,
                                onClick = { onRecipeClick(meal.id) },
                                onFavoriteClick = { onFavoriteClick(meal.id) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Today's Meal Plan Summary
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "Today's Plan 📅",
                        actionText = "View All",
                        onActionClick = onMealPlannerClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TodayMealPlanCard(onMealPlannerClick = onMealPlannerClick)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Grocery List Summary
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "Grocery List 🛒",
                        actionText = "View All",
                        onActionClick = onGroceryListClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GroceryListSummaryCard(onGroceryListClick = onGroceryListClick)
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    feature: QuickAccessFeature,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = feature.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = feature.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun RecipeCategoryChip(
    category: RecipeCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = category.color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = category.emoji, fontSize = 18.sp)
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ApiCategoryChip(
    name: String,
    emoji: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = emoji, fontSize = 18.sp)
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: RecipePreview,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Recipe Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(GreenLight.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍽️", fontSize = 40.sp)
                // Favorite icon
                Icon(
                    imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (recipe.isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Orange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = recipe.rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = recipe.prepTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun PopularMealCard(
    meal: MealPreview,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Meal Image from API
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(meal.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = meal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                                startY = 50f
                            )
                        )
                )
                // Favorite icon
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (meal.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (meal.isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = meal.category ?: meal.area ?: "Recipe",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreenPrimary
                )
            }
        }
    }
}

@Composable
fun TodayMealPlanCard(
    onMealPlannerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMealPlannerClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Orange.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "No meals planned for today",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap to add breakfast, lunch & dinner",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Orange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Plan",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GroceryListSummaryCard(
    onGroceryListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onGroceryListClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Your grocery list is empty",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add items from recipes or manually",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Grocery",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    EatsBuddyTheme {
        HomePage()
    }
}

@Preview(showBackground = true)
@Composable
fun QuickAccessCardPreview() {
    EatsBuddyTheme {
        QuickAccessCard(
            feature = QuickAccessFeature(
                title = "Recipes",
                subtitle = "Browse & Save",
                icon = Icons.Default.Search,
                backgroundColor = GreenPrimary
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeCardPreview() {
    EatsBuddyTheme {
        RecipeCard(
            recipe = RecipePreview(
                id = 1,
                name = "Spaghetti Carbonara",
                category = "Italian",
                rating = 4.8f,
                prepTime = "30 min",
                isFavorite = true
            ),
            onClick = {}
        )
    }
}

private fun getInitials(firstName: String?, lastName: String?): String {
    val first = firstName?.firstOrNull()?.uppercaseChar() ?: ""
    val last = lastName?.firstOrNull()?.uppercaseChar() ?: ""
    return "$first$last".ifEmpty { "?" }
}
