package com.example.eatsbuddy.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsPage(
    recipeId: Int,
    onBackClick: () -> Unit = {},
    onAddToGroceryList: (List<String>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // In a real app, you'd fetch this from a repository/database
    var recipe by remember {
        mutableStateOf(
            getSampleRecipeById(recipeId)
        )
    }
    
    var isFavorite by remember { mutableStateOf(recipe?.isFavorite ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
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
                    IconButton(onClick = { /* Share recipe */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onPrimary
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
        if (recipe != null) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Hero Image Section
                item {
                    RecipeHeroSection(recipe = recipe!!)
                }

                // Quick Info Cards
                item {
                    QuickInfoSection(recipe = recipe!!)
                }

                // Description
                item {
                    DescriptionSection(description = recipe!!.description)
                }

                // Ingredients Section
                item {
                    IngredientsSection(
                        ingredients = recipe!!.ingredients,
                        onAddToGroceryList = { onAddToGroceryList(recipe!!.ingredients) }
                    )
                }

                // Instructions Section
                item {
                    InstructionsHeader()
                }

                itemsIndexed(recipe!!.instructions) { index, instruction ->
                    InstructionStep(
                        stepNumber = index + 1,
                        instruction = instruction
                    )
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            // Recipe not found
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "😕", fontSize = 60.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recipe not found",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeHeroSection(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        GreenLight.copy(alpha = 0.3f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recipe.imageEmoji,
                fontSize = 80.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Rating Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Orange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${recipe.rating}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " (${recipe.reviewCount} reviews)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun QuickInfoSection(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickInfoCard(
            emoji = "⏱️",
            label = "Prep",
            value = recipe.prepTime
        )
        QuickInfoCard(
            emoji = "🍳",
            label = "Cook",
            value = recipe.cookTime
        )
        QuickInfoCard(
            emoji = "🍽️",
            label = "Servings",
            value = recipe.servings.toString()
        )
        QuickInfoCard(
            emoji = "🔥",
            label = "Calories",
            value = "${recipe.calories}"
        )
    }
}

@Composable
fun QuickInfoCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun DescriptionSection(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun IngredientsSection(
    ingredients: List<String>,
    onAddToGroceryList: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🥘", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Add to Grocery List button
                Card(
                    onClick = onAddToGroceryList,
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🛒", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add All",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ingredients.forEach { ingredient ->
                IngredientItem(ingredient = ingredient)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun IngredientItem(
    ingredient: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(GreenPrimary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = ingredient,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun InstructionsHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "👨‍🍳", fontSize = 24.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InstructionStep(
    stepNumber: Int,
    instruction: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Step Number Circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = instruction,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                lineHeight = 22.sp
            )
        }
    }
}

// Sample recipe data (in real app, this would come from database/API)
private fun getSampleRecipeById(id: Int): Recipe? {
    val sampleRecipes = listOf(
        Recipe(
            id = 1,
            name = "Spaghetti Carbonara",
            description = "A classic Italian pasta dish from Rome made with eggs, hard cheese, cured pork, and black pepper. The creamy sauce is made without any cream - just eggs and cheese create the silky texture.",
            category = "Dinner",
            rating = 4.8f,
            reviewCount = 256,
            prepTime = "15 min",
            cookTime = "15 min",
            servings = 4,
            calories = 450,
            difficulty = "Medium",
            ingredients = listOf(
                "400g spaghetti",
                "200g guanciale or bacon",
                "4 large eggs",
                "100g Pecorino Romano cheese",
                "50g Parmesan cheese",
                "Freshly ground black pepper",
                "Salt for pasta water"
            ),
            instructions = listOf(
                "Bring a large pot of salted water to boil and cook spaghetti according to package directions until al dente.",
                "While pasta cooks, cut guanciale into small strips and cook in a large pan over medium heat until crispy.",
                "In a bowl, whisk together eggs, Pecorino, Parmesan, and plenty of black pepper.",
                "When pasta is done, reserve 1 cup of pasta water, then drain.",
                "Remove pan from heat and add hot pasta to the guanciale. Toss well.",
                "Quickly pour egg mixture over pasta, tossing constantly to create a creamy sauce. Add pasta water if needed.",
                "Serve immediately with extra cheese and black pepper on top."
            ),
            isFavorite = true,
            imageEmoji = "🍝"
        ),
        Recipe(
            id = 2,
            name = "Chicken Stir Fry",
            description = "A quick and healthy Asian-style dish featuring tender chicken pieces and colorful vegetables in a savory sauce. Perfect for busy weeknights when you want something nutritious and delicious.",
            category = "Dinner",
            rating = 4.5f,
            reviewCount = 189,
            prepTime = "10 min",
            cookTime = "15 min",
            servings = 3,
            calories = 320,
            difficulty = "Easy",
            ingredients = listOf(
                "500g chicken breast, sliced",
                "2 cups mixed vegetables (bell peppers, broccoli, snap peas)",
                "3 tbsp soy sauce",
                "1 tbsp sesame oil",
                "2 cloves garlic, minced",
                "1 inch ginger, grated",
                "2 tbsp vegetable oil",
                "1 tbsp cornstarch",
                "Green onions for garnish"
            ),
            instructions = listOf(
                "Mix chicken with 1 tbsp soy sauce and cornstarch. Let marinate for 10 minutes.",
                "Heat vegetable oil in a wok or large pan over high heat.",
                "Add chicken and stir-fry until golden brown. Remove and set aside.",
                "Add more oil if needed, then stir-fry garlic and ginger for 30 seconds.",
                "Add vegetables and stir-fry for 3-4 minutes until crisp-tender.",
                "Return chicken to the pan. Add remaining soy sauce and sesame oil.",
                "Toss everything together and serve over rice, garnished with green onions."
            ),
            isFavorite = false,
            imageEmoji = "🍗"
        ),
        Recipe(
            id = 3,
            name = "Greek Salad",
            description = "A refreshing Mediterranean salad featuring crisp vegetables, creamy feta cheese, and briny olives dressed in olive oil and herbs. A perfect light lunch or side dish.",
            category = "Lunch",
            rating = 4.6f,
            reviewCount = 145,
            prepTime = "15 min",
            cookTime = "0 min",
            servings = 2,
            calories = 180,
            difficulty = "Easy",
            ingredients = listOf(
                "4 ripe tomatoes, cut into wedges",
                "1 cucumber, sliced",
                "1 red onion, thinly sliced",
                "200g feta cheese, cubed",
                "1 cup Kalamata olives",
                "4 tbsp extra virgin olive oil",
                "2 tbsp red wine vinegar",
                "1 tsp dried oregano",
                "Salt and pepper to taste"
            ),
            instructions = listOf(
                "Cut tomatoes into wedges and place in a large serving bowl.",
                "Slice cucumber and red onion, add to the bowl.",
                "Add Kalamata olives on top.",
                "Place cubed feta cheese over the vegetables.",
                "Drizzle with olive oil and red wine vinegar.",
                "Sprinkle with oregano, salt, and pepper.",
                "Toss gently just before serving to keep feta intact."
            ),
            isFavorite = true,
            imageEmoji = "🥗"
        ),
        Recipe(
            id = 4,
            name = "Banana Pancakes",
            description = "Fluffy and naturally sweet pancakes made with ripe bananas. These wholesome breakfast treats are perfect for a lazy weekend morning and loved by both kids and adults alike.",
            category = "Breakfast",
            rating = 4.7f,
            reviewCount = 312,
            prepTime = "10 min",
            cookTime = "10 min",
            servings = 2,
            calories = 280,
            difficulty = "Easy",
            ingredients = listOf(
                "2 ripe bananas",
                "2 large eggs",
                "1 cup all-purpose flour",
                "3/4 cup milk",
                "1 tsp baking powder",
                "1/2 tsp cinnamon",
                "1 tbsp honey or maple syrup",
                "Butter for cooking",
                "Fresh berries for topping"
            ),
            instructions = listOf(
                "Mash bananas in a large bowl until smooth with only small lumps remaining.",
                "Whisk in eggs, milk, and honey until well combined.",
                "Add flour, baking powder, and cinnamon. Mix until just combined - don't overmix!",
                "Heat a non-stick pan or griddle over medium heat. Add a little butter.",
                "Pour about 1/4 cup batter for each pancake. Cook until bubbles form on surface.",
                "Flip and cook for another 1-2 minutes until golden brown.",
                "Serve warm with maple syrup, fresh berries, and sliced bananas."
            ),
            isFavorite = false,
            imageEmoji = "🥞"
        ),
        Recipe(
            id = 5,
            name = "Chocolate Lava Cake",
            description = "An indulgent dessert with a warm, gooey chocolate center that flows like lava when you cut into it. Surprisingly easy to make and guaranteed to impress your guests.",
            category = "Dessert",
            rating = 4.9f,
            reviewCount = 423,
            prepTime = "15 min",
            cookTime = "12 min",
            servings = 4,
            calories = 380,
            difficulty = "Medium",
            ingredients = listOf(
                "200g dark chocolate (70% cocoa)",
                "100g butter",
                "2 large eggs",
                "2 egg yolks",
                "50g sugar",
                "2 tbsp flour",
                "Butter and cocoa for ramekins",
                "Vanilla ice cream for serving"
            ),
            instructions = listOf(
                "Preheat oven to 425°F (220°C). Butter 4 ramekins and dust with cocoa powder.",
                "Melt chocolate and butter together in a double boiler or microwave. Stir until smooth.",
                "In a bowl, whisk eggs, egg yolks, and sugar until thick and pale.",
                "Fold the chocolate mixture into the egg mixture.",
                "Sift in flour and fold gently until just combined.",
                "Divide batter among prepared ramekins.",
                "Bake for 12-14 minutes until edges are firm but center is soft.",
                "Let cool for 1 minute, then invert onto plates. Serve immediately with ice cream."
            ),
            isFavorite = true,
            imageEmoji = "🍫"
        ),
        Recipe(
            id = 6,
            name = "Avocado Toast",
            description = "A simple yet satisfying breakfast staple that's both nutritious and delicious. Customize with your favorite toppings for endless variations.",
            category = "Breakfast",
            rating = 4.3f,
            reviewCount = 178,
            prepTime = "5 min",
            cookTime = "2 min",
            servings = 1,
            calories = 220,
            difficulty = "Easy",
            ingredients = listOf(
                "2 slices sourdough bread",
                "1 ripe avocado",
                "1/2 lemon, juiced",
                "Red pepper flakes",
                "Sea salt and black pepper",
                "Everything bagel seasoning (optional)",
                "Cherry tomatoes (optional)"
            ),
            instructions = listOf(
                "Toast bread slices until golden and crispy.",
                "Cut avocado in half, remove pit, and scoop flesh into a bowl.",
                "Add lemon juice, salt, and pepper. Mash with a fork to desired consistency.",
                "Spread mashed avocado generously on warm toast.",
                "Top with red pepper flakes and everything bagel seasoning.",
                "Add halved cherry tomatoes if desired.",
                "Serve immediately while toast is still warm."
            ),
            isFavorite = false,
            imageEmoji = "🥑"
        ),
        Recipe(
            id = 7,
            name = "Mango Smoothie",
            description = "A refreshing tropical smoothie that tastes like sunshine in a glass. Perfect for hot summer days or whenever you need a healthy pick-me-up.",
            category = "Drinks",
            rating = 4.4f,
            reviewCount = 98,
            prepTime = "5 min",
            cookTime = "0 min",
            servings = 2,
            calories = 150,
            difficulty = "Easy",
            ingredients = listOf(
                "2 ripe mangoes, cubed (or 2 cups frozen)",
                "1 cup Greek yogurt",
                "1 cup milk or coconut milk",
                "2 tbsp honey",
                "1/2 cup ice cubes",
                "Fresh mint for garnish"
            ),
            instructions = listOf(
                "If using fresh mangoes, peel, pit, and cut into cubes.",
                "Add mango, yogurt, milk, and honey to a blender.",
                "Add ice cubes for a thicker, colder smoothie.",
                "Blend on high until completely smooth and creamy.",
                "Taste and adjust sweetness with more honey if needed.",
                "Pour into glasses and garnish with fresh mint.",
                "Serve immediately for best texture."
            ),
            isFavorite = false,
            imageEmoji = "🥤"
        ),
        Recipe(
            id = 8,
            name = "Crispy Popcorn",
            description = "The ultimate movie night snack made on the stovetop. Season it your way for sweet, savory, or spicy perfection.",
            category = "Snacks",
            rating = 4.2f,
            reviewCount = 67,
            prepTime = "2 min",
            cookTime = "5 min",
            servings = 4,
            calories = 120,
            difficulty = "Easy",
            ingredients = listOf(
                "1/2 cup popcorn kernels",
                "3 tbsp vegetable or coconut oil",
                "3 tbsp melted butter",
                "Salt to taste",
                "Optional: nutritional yeast, parmesan, or cinnamon sugar"
            ),
            instructions = listOf(
                "Add oil to a large pot with a lid over medium-high heat.",
                "Put 3 kernels in the oil and cover. Wait for them to pop.",
                "Once test kernels pop, add remaining kernels in an even layer.",
                "Cover and shake pot occasionally while kernels pop.",
                "When popping slows to 2-3 seconds between pops, remove from heat.",
                "Transfer to a large bowl and drizzle with melted butter.",
                "Season with salt and your choice of toppings. Toss to coat evenly."
            ),
            isFavorite = false,
            imageEmoji = "🍿"
        )
    )
    
    return sampleRecipes.find { it.id == id }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailsPagePreview() {
    EatsBuddyTheme {
        RecipeDetailsPage(recipeId = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun QuickInfoCardPreview() {
    EatsBuddyTheme {
        QuickInfoCard(
            emoji = "⏱️",
            label = "Prep",
            value = "15 min"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionStepPreview() {
    EatsBuddyTheme {
        InstructionStep(
            stepNumber = 1,
            instruction = "Bring a large pot of salted water to boil and cook spaghetti according to package directions."
        )
    }
}
