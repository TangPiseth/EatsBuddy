package com.example.eatsbuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Simple date holder class (year, month, day)
data class SimpleDate(
    val year: Int,
    val month: Int, // 0-indexed (January = 0)
    val day: Int
) {
    fun toKey(): String = "$year-$month-$day"
    
    companion object {
        fun today(): SimpleDate {
            val cal = Calendar.getInstance()
            return SimpleDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}

// Data class for a planned meal
data class PlannedMeal(
    val id: String,
    val date: SimpleDate,
    val mealTime: MealTime,
    val mealName: String
)

enum class MealTime(val displayName: String, val emoji: String, val color: Color) {
    BREAKFAST("Breakfast", "🌅", Color(0xFFFFB74D)),
    LUNCH("Lunch", "☀️", Color(0xFF81C784)),
    DINNER("Dinner", "🌙", Color(0xFF64B5F6))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerPage(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val today = remember { SimpleDate.today() }
    var currentYear by remember { mutableIntStateOf(today.year) }
    var currentMonth by remember { mutableIntStateOf(today.month) }
    var selectedDate by remember { mutableStateOf(today) }
    var plannedMeals by remember { mutableStateOf<List<PlannedMeal>>(emptyList()) }
    var showAddMealDialog by remember { mutableStateOf(false) }
    var selectedMealTime by remember { mutableStateOf<MealTime?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Meal Planner 📅",
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
            contentPadding = PaddingValues(16.dp)
        ) {
            // Calendar Section
            item {
                CalendarCard(
                    currentYear = currentYear,
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    today = today,
                    plannedMeals = plannedMeals,
                    onDateSelected = { selectedDate = it },
                    onPreviousMonth = {
                        if (currentMonth == 0) {
                            currentMonth = 11
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    },
                    onNextMonth = {
                        if (currentMonth == 11) {
                            currentMonth = 0
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Selected Date Header
            item {
                Text(
                    text = formatSelectedDate(selectedDate),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Meal Time Slots
            item {
                MealTime.entries.forEach { mealTime ->
                    val mealsForTime = plannedMeals.filter {
                        it.date == selectedDate && it.mealTime == mealTime
                    }
                    MealTimeSlot(
                        mealTime = mealTime,
                        meals = mealsForTime,
                        onAddMeal = {
                            selectedMealTime = mealTime
                            showAddMealDialog = true
                        },
                        onDeleteMeal = { mealId ->
                            plannedMeals = plannedMeals.filter { it.id != mealId }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    // Add Meal Dialog
    if (showAddMealDialog && selectedMealTime != null) {
        AddMealDialog(
            mealTime = selectedMealTime!!,
            selectedDate = selectedDate,
            onDismiss = {
                showAddMealDialog = false
                selectedMealTime = null
            },
            onConfirm = { mealName ->
                val newMeal = PlannedMeal(
                    id = "${selectedDate.toKey()}_${selectedMealTime}_${System.currentTimeMillis()}",
                    date = selectedDate,
                    mealTime = selectedMealTime!!,
                    mealName = mealName
                )
                plannedMeals = plannedMeals + newMeal
                showAddMealDialog = false
                selectedMealTime = null
            }
        )
    }
}

@Composable
fun CalendarCard(
    currentYear: Int,
    currentMonth: Int,
    selectedDate: SimpleDate,
    today: SimpleDate,
    plannedMeals: List<PlannedMeal>,
    onDateSelected: (SimpleDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Month Navigation Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousMonth) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = GreenPrimary
                    )
                }

                Text(
                    text = "${getMonthName(currentMonth)} $currentYear",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onNextMonth) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = GreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Day of Week Headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            val calendarDays = getCalendarDays(currentYear, currentMonth)

            // Display calendar in rows of 7
            calendarDays.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    week.forEach { date ->
                        CalendarDay(
                            date = date,
                            isSelected = date == selectedDate,
                            isToday = date == today,
                            hasMeals = date != null && plannedMeals.any { it.date == date },
                            onDateSelected = { date?.let { onDateSelected(it) } },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDay(
    date: SimpleDate?,
    isSelected: Boolean,
    isToday: Boolean,
    hasMeals: Boolean,
    onDateSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> GreenPrimary
                    isToday -> GreenLight.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = date != null) { onDateSelected() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (date != null) {
                Text(
                    text = date.day.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isSelected -> Color.White
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
                // Meal indicator dot
                if (hasMeals && !isSelected) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Orange)
                    )
                }
            }
        }
    }
}

@Composable
fun MealTimeSlot(
    mealTime: MealTime,
    meals: List<PlannedMeal>,
    onAddMeal: () -> Unit,
    onDeleteMeal: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = mealTime.color.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mealTime.emoji,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mealTime.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onAddMeal,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(mealTime.color)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add ${mealTime.displayName}",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Meals list or empty state
            if (meals.isEmpty()) {
                Text(
                    text = "No meal planned",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                meals.forEach { meal ->
                    MealItem(
                        meal = meal,
                        onDelete = { onDeleteMeal(meal.id) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun MealItem(
    meal: PlannedMeal,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = meal.mealName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun AddMealDialog(
    mealTime: MealTime,
    selectedDate: SimpleDate,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var mealName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = mealTime.emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add ${mealTime.displayName}",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                Text(
                    text = formatSelectedDate(selectedDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = { Text("Meal / Food Name") },
                    placeholder = { Text("e.g., Pancakes, Salad...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (mealName.isNotBlank()) {
                        onConfirm(mealName.trim())
                    }
                },
                enabled = mealName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
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

// Helper function to get month name
private fun getMonthName(month: Int): String {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    return months[month]
}

// Helper function to get day of week name
private fun getDayOfWeekName(year: Int, month: Int, day: Int): String {
    val cal = Calendar.getInstance().apply {
        set(year, month, day)
    }
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    return days[dayOfWeek - 1]
}

// Helper function to format selected date
private fun formatSelectedDate(date: SimpleDate): String {
    val dayOfWeek = getDayOfWeekName(date.year, date.month, date.day)
    val monthName = getMonthName(date.month)
    return "$dayOfWeek, $monthName ${date.day}, ${date.year}"
}

// Helper function to get calendar days for a month
private fun getCalendarDays(year: Int, month: Int): List<SimpleDate?> {
    val cal = Calendar.getInstance().apply {
        set(year, month, 1)
    }
    
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // Sunday = 0
    
    val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7
    
    return (0 until totalCells).map { index ->
        val dayOffset = index - firstDayOfWeek
        if (dayOffset in 0 until daysInMonth) {
            SimpleDate(year, month, dayOffset + 1)
        } else {
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MealPlannerPagePreview() {
    EatsBuddyTheme {
        MealPlannerPage()
    }
}

@Preview(showBackground = true)
@Composable
fun MealTimeSlotPreview() {
    EatsBuddyTheme {
        MealTimeSlot(
            mealTime = MealTime.BREAKFAST,
            meals = listOf(
                PlannedMeal("1", SimpleDate(2025, 11, 1), MealTime.BREAKFAST, "Pancakes with Maple Syrup")
            ),
            onAddMeal = {},
            onDeleteMeal = {}
        )
    }
}
