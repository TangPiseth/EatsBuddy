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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange

// Data class for grocery item
data class GroceryItem(
    val id: String,
    val name: String,
    val quantity: String,
    val category: GroceryCategory,
    val isChecked: Boolean = false
)

// Categories for grocery items
enum class GroceryCategory(
    val displayName: String,
    val emoji: String,
    val color: Color
) {
    FRUITS_VEGETABLES("Fruits & Veggies", "🥬", Color(0xFF4CAF50)),
    MEAT_SEAFOOD("Meat & Seafood", "🥩", Color(0xFFE57373)),
    DAIRY_EGGS("Dairy & Eggs", "🥛", Color(0xFF64B5F6)),
    BAKERY("Bakery", "🍞", Color(0xFFFFB74D)),
    PANTRY("Pantry", "🥫", Color(0xFF9575CD)),
    FROZEN("Frozen", "🧊", Color(0xFF4DD0E1)),
    BEVERAGES("Beverages", "🥤", Color(0xFFFF8A65)),
    SNACKS("Snacks", "🍿", Color(0xFFFFD54F)),
    OTHER("Other", "📦", Color(0xFF90A4AE))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListPage(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var groceryItems by remember { mutableStateOf<List<GroceryItem>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<GroceryCategory?>(null) }
    var quickAddText by remember { mutableStateOf("") }

    // Filter items by category if selected
    val displayedItems = if (selectedCategory != null) {
        groceryItems.filter { it.category == selectedCategory }
    } else {
        groceryItems
    }

    // Group items by category for display
    val groupedItems = displayedItems.groupBy { it.category }

    // Calculate progress
    val checkedCount = groceryItems.count { it.isChecked }
    val totalCount = groceryItems.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grocery List 🛒",
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
                actions = {
                    // Clear all checked items
                    if (checkedCount > 0) {
                        IconButton(
                            onClick = {
                                groceryItems = groceryItems.filter { !it.isChecked }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear checked",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = GreenPrimary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Progress Card
            item {
                ProgressCard(
                    checkedCount = checkedCount,
                    totalCount = totalCount
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quick Add Section
            item {
                QuickAddSection(
                    value = quickAddText,
                    onValueChange = { quickAddText = it },
                    onAdd = {
                        if (quickAddText.isNotBlank()) {
                            val newItem = GroceryItem(
                                id = System.currentTimeMillis().toString(),
                                name = quickAddText.trim(),
                                quantity = "",
                                category = GroceryCategory.OTHER
                            )
                            groceryItems = groceryItems + newItem
                            quickAddText = ""
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Category Filter
            item {
                Text(
                    text = "Filter by Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // All category chip
                    item {
                        CategoryFilterChip(
                            label = "All",
                            emoji = "📋",
                            isSelected = selectedCategory == null,
                            color = GreenPrimary,
                            onClick = { selectedCategory = null }
                        )
                    }
                    items(GroceryCategory.entries.toList()) { category ->
                        CategoryFilterChip(
                            label = category.displayName,
                            emoji = category.emoji,
                            isSelected = selectedCategory == category,
                            color = category.color,
                            onClick = {
                                selectedCategory = if (selectedCategory == category) null else category
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Empty State
            if (groceryItems.isEmpty()) {
                item {
                    EmptyGroceryState()
                }
            } else if (displayedItems.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No items in this category",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            } else {
                // Grocery Items grouped by category
                groupedItems.forEach { (category, items) ->
                    item {
                        CategoryHeader(category = category, itemCount = items.size)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(items, key = { it.id }) { item ->
                        GroceryItemCard(
                            item = item,
                            onCheckedChange = { isChecked ->
                                groceryItems = groceryItems.map {
                                    if (it.id == item.id) it.copy(isChecked = isChecked) else it
                                }
                            },
                            onDelete = {
                                groceryItems = groceryItems.filter { it.id != item.id }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Add Item Dialog
    if (showAddDialog) {
        AddGroceryItemDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, quantity, category ->
                val newItem = GroceryItem(
                    id = System.currentTimeMillis().toString(),
                    name = name,
                    quantity = quantity,
                    category = category
                )
                groceryItems = groceryItems + newItem
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ProgressCard(
    checkedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalCount > 0) checkedCount.toFloat() / totalCount else 0f

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GreenPrimary.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Shopping Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$checkedCount of $totalCount items",
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
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(GreenPrimary)
                )
            }
        }
    }
}

@Composable
fun QuickAddSection(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Quick add item...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAdd() }),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CategoryFilterChip(
    label: String,
    emoji: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) color else color.copy(alpha = 0.1f),
        label = "chipBackground"
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CategoryHeader(
    category: GroceryCategory,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category.emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(category.color.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = itemCount.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = category.color
            )
        }
    }
}

@Composable
fun GroceryItemCard(
    item: GroceryItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isChecked)
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = GreenPrimary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                    color = if (item.isChecked)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.quantity.isNotBlank()) {
                    Text(
                        text = item.quantity,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Category indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(item.category.color)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyGroceryState(
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
            Text(text = "🛒", fontSize = 60.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your grocery list is empty",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start adding items using the quick add\nor tap the + button for more options",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun AddGroceryItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, quantity: String, category: GroceryCategory) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(GroceryCategory.OTHER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Grocery Item 🛒",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    placeholder = { Text("e.g., Milk, Apples...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (optional)") },
                    placeholder = { Text("e.g., 2 lbs, 1 dozen...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    GroceryCategory.entries.chunked(3).forEach { rowCategories ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowCategories.forEach { category ->
                                CategorySelectChip(
                                    category = category,
                                    isSelected = selectedCategory == category,
                                    onClick = { selectedCategory = category },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill empty space if row is not complete
                            repeat(3 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (itemName.isNotBlank()) {
                        onConfirm(itemName.trim(), quantity.trim(), selectedCategory)
                    }
                },
                enabled = itemName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CategorySelectChip(
    category: GroceryCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) category.color else category.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = category.emoji, fontSize = 20.sp)
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryListPagePreview() {
    EatsBuddyTheme {
        GroceryListPage()
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryItemCardPreview() {
    EatsBuddyTheme {
        GroceryItemCard(
            item = GroceryItem(
                id = "1",
                name = "Organic Milk",
                quantity = "1 gallon",
                category = GroceryCategory.DAIRY_EGGS,
                isChecked = false
            ),
            onCheckedChange = {},
            onDelete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyGroceryStatePreview() {
    EatsBuddyTheme {
        EmptyGroceryState()
    }
}
