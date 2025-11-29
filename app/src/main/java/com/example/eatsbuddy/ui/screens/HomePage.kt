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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.components.CategoryItem
import com.example.eatsbuddy.ui.components.FeaturedRestaurantCard
import com.example.eatsbuddy.ui.components.FoodCategory
import com.example.eatsbuddy.ui.components.Restaurant
import com.example.eatsbuddy.ui.components.RestaurantCard
import com.example.eatsbuddy.ui.components.SearchBar
import com.example.eatsbuddy.ui.components.SectionHeader
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange
import com.example.eatsbuddy.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember {
        listOf(
            FoodCategory("All", Icons.Default.Home, GreenPrimary),
            FoodCategory("Pizza", Icons.Default.Favorite, Orange),
            FoodCategory("Burger", Icons.Default.Star, Red),
            FoodCategory("Asian", Icons.Default.ThumbUp, GreenLight),
            FoodCategory("Coffee", Icons.Default.ShoppingCart, Color(0xFF795548)),
            FoodCategory("Dessert", Icons.Default.Favorite, Color(0xFFE91E63)),
            FoodCategory("Fast Food", Icons.Default.Place, Color(0xFFFF5722)),
            FoodCategory("Drinks", Icons.Default.Info, Color(0xFF9C27B0))
        )
    }

    val featuredRestaurants = remember {
        listOf(
            Restaurant(
                id = 1,
                name = "Green Garden Bistro",
                cuisine = "Healthy • Organic • Salads",
                rating = 4.8f,
                reviewCount = 256,
                distance = "0.5 km",
                priceRange = "$$",
                imageUrl = "",
                isOpen = true
            ),
            Restaurant(
                id = 2,
                name = "Sakura Japanese",
                cuisine = "Japanese • Sushi • Ramen",
                rating = 4.7f,
                reviewCount = 189,
                distance = "1.2 km",
                priceRange = "$$$",
                imageUrl = "",
                isOpen = true
            ),
            Restaurant(
                id = 3,
                name = "Mama's Italian Kitchen",
                cuisine = "Italian • Pizza • Pasta",
                rating = 4.6f,
                reviewCount = 342,
                distance = "0.8 km",
                priceRange = "$$",
                imageUrl = "",
                isOpen = true
            )
        )
    }

    val nearbyRestaurants = remember {
        listOf(
            Restaurant(
                id = 4,
                name = "The Breakfast Club",
                cuisine = "American • Breakfast • Brunch",
                rating = 4.5f,
                reviewCount = 178,
                distance = "0.3 km",
                priceRange = "$",
                imageUrl = "",
                isOpen = true
            ),
            Restaurant(
                id = 5,
                name = "Spice Route",
                cuisine = "Indian • Curry • Tandoori",
                rating = 4.4f,
                reviewCount = 215,
                distance = "0.6 km",
                priceRange = "$$",
                imageUrl = "",
                isOpen = true
            ),
            Restaurant(
                id = 6,
                name = "Taco Fiesta",
                cuisine = "Mexican • Tacos • Burritos",
                rating = 4.3f,
                reviewCount = 167,
                distance = "0.9 km",
                priceRange = "$",
                imageUrl = "",
                isOpen = false
            ),
            Restaurant(
                id = 7,
                name = "Seoul Kitchen",
                cuisine = "Korean • BBQ • Kimchi",
                rating = 4.6f,
                reviewCount = 198,
                distance = "1.1 km",
                priceRange = "$$",
                imageUrl = "",
                isOpen = true
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "📍 Current Location",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Phnom Penh, Cambodia",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle notifications */ }) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Orange
                                ) {
                                    Text("3")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Profile avatar
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👤",
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
            // Welcome Banner
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
                            text = "Good Morning! 👋",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "What would you like to eat today?",
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

            // Categories
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    SectionHeader(
                        title = "Categories",
                        actionText = "See All",
                        onActionClick = { /* Navigate to categories */ }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onClick = { /* Handle category click */ }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Featured Restaurants
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    SectionHeader(
                        title = "Featured 🔥",
                        actionText = "See All",
                        onActionClick = { /* Navigate to featured */ }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(featuredRestaurants) { restaurant ->
                        FeaturedRestaurantCard(
                            restaurant = restaurant,
                            onClick = { /* Navigate to restaurant details */ }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Nearby Restaurants
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    SectionHeader(
                        title = "Nearby You 📍",
                        actionText = "See All",
                        onActionClick = { /* Navigate to nearby */ }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(nearbyRestaurants) { restaurant ->
                RestaurantCard(
                    restaurant = restaurant,
                    onClick = { /* Navigate to restaurant details */ },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp)
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
