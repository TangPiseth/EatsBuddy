package com.example.eatsbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eatsbuddy.ui.screens.GroceryListPage
import com.example.eatsbuddy.ui.screens.HomePage
import com.example.eatsbuddy.ui.screens.LoginScreen
import com.example.eatsbuddy.ui.screens.MealPlannerPage
import com.example.eatsbuddy.ui.screens.RegisterScreen
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EatsBuddyTheme(darkTheme = false) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EatsBuddyApp()
                }
            }
        }
    }
}

@Composable
fun EatsBuddyApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomePage(
                onProfileClick = {
                    navController.navigate("register")
                },
                onRecipesClick = {
                    // TODO: Navigate to recipes page
                },
                onMealPlannerClick = {
                    navController.navigate("mealPlanner")
                },
                onGroceryListClick = {
                    navController.navigate("groceryList")
                },
                onSearchClick = {
                    // TODO: Handle search
                },
                onRecipeClick = { recipeId ->
                    // TODO: Navigate to recipe details
                },
                onCategoryClick = { category ->
                    // TODO: Navigate to category filtered recipes
                }
            )
        }
        
        composable("mealPlanner") {
            MealPlannerPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("groceryList") {
            GroceryListPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("register") {
            RegisterScreen(
                onRegisterClick = { email, password, confirmPassword ->
                    // Handle registration logic here
                    // For now, navigate to home after registration
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("login") {
            LoginScreen(
                onLoginClick = { email, password ->
                    // Handle login logic here
                    // For now, navigate to home after login
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    // Handle forgot password
                },
                onSignUpClick = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}