package com.example.eatsbuddy.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenPrimary

data class FAQItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQPage(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val faqItems = listOf(
        FAQItem(
            question = "How do I save a recipe to my favorites?",
            answer = "Simply tap the heart icon on any recipe card or on the recipe details page. The recipe will be added to your favorites and you can access them anytime by filtering recipes by 'Favorites' in the Recipes page."
        ),
        FAQItem(
            question = "Can I create my own recipes?",
            answer = "Currently, EatsBuddy provides a curated collection of recipes. We're working on adding the ability for users to create and share their own recipes in a future update!"
        ),
        FAQItem(
            question = "How does the meal planner work?",
            answer = "Navigate to the Meal Planner from the home screen or bottom navigation. Select a date on the calendar, then tap the '+' button next to Breakfast, Lunch, or Dinner to add a meal. You can add multiple items to each meal time."
        ),
        FAQItem(
            question = "How do I add items to my grocery list?",
            answer = "There are two ways: 1) Use the quick add bar at the top of the Grocery List page to type items directly. 2) From any recipe details page, tap 'Add All' to add all ingredients to your grocery list."
        ),
        FAQItem(
            question = "Can I organize my grocery list by category?",
            answer = "Yes! When adding items through the '+' button, you can select a category for each item. Items are automatically grouped by category. You can also filter by category using the chips at the top of the list."
        ),
        FAQItem(
            question = "How do I mark items as purchased?",
            answer = "Simply tap the checkbox next to any grocery item. The item will be crossed out. To remove all checked items at once, tap the clear button in the top right corner of the Grocery List page."
        ),
        FAQItem(
            question = "Is my data saved when I close the app?",
            answer = "Currently, data is stored locally during your session. For persistent storage across sessions, please create an account and log in. We're continuously improving our data storage features."
        ),
        FAQItem(
            question = "How can I search for specific recipes?",
            answer = "Use the search bar at the top of the Recipes page. You can search by recipe name, ingredients, or description. Combine search with category filters and sorting options to find exactly what you're looking for."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FAQ ❓",
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
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Find answers to common questions about EatsBuddy",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(faqItems) { index, item ->
                FAQCard(
                    index = index + 1,
                    item = item
                )
            }
        }
    }
}

@Composable
fun FAQCard(
    index: Int,
    item: FAQItem,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) 
                GreenPrimary.copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$index.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = item.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.KeyboardArrowUp 
                    else 
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = GreenPrimary
                )
            }
            
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FAQPagePreview() {
    EatsBuddyTheme {
        FAQPage()
    }
}
