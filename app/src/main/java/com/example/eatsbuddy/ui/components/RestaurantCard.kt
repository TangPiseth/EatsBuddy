package com.example.eatsbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange

data class Restaurant(
    val id: Int,
    val name: String,
    val cuisine: String,
    val rating: Float,
    val reviewCount: Int,
    val distance: String,
    val priceRange: String,
    val imageUrl: String,
    val isOpen: Boolean = true
)

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image placeholder with gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                GreenPrimary.copy(alpha = 0.3f),
                                GreenLight.copy(alpha = 0.5f)
                            )
                        )
                    )
            ) {
                // Status badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (restaurant.isOpen) GreenPrimary else Color.Gray
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (restaurant.isOpen) "Open" else "Closed",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Restaurant icon placeholder
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = restaurant.name.take(1),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            modifier = Modifier.size(18.dp),
                            tint = Orange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurant.rating.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = " (${restaurant.reviewCount})",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = restaurant.cuisine,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Distance",
                            modifier = Modifier.size(16.dp),
                            tint = GreenPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurant.distance,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = restaurant.priceRange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = GreenPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedRestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Image placeholder with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                GreenPrimary,
                                GreenLight
                            )
                        )
                    )
            ) {
                // Featured badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Orange)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⭐ Featured",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Restaurant initial
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = restaurant.name.take(1),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = restaurant.cuisine,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            modifier = Modifier.size(16.dp),
                            tint = Orange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${restaurant.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Distance",
                            modifier = Modifier.size(14.dp),
                            tint = GreenPrimary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = restaurant.distance,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = restaurant.priceRange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GreenPrimary
                    )
                }
            }
        }
    }
}
