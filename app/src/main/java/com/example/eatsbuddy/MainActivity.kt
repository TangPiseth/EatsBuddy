package com.example.eatsbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eatsbuddy.ui.screens.GroceryListPage
import com.example.eatsbuddy.ui.screens.HomePage
import com.example.eatsbuddy.ui.screens.LoginScreen
import com.example.eatsbuddy.ui.screens.MealPlannerPage
import com.example.eatsbuddy.ui.screens.ProfileScreen
import com.example.eatsbuddy.ui.screens.ProfileSetupScreen
import com.example.eatsbuddy.ui.screens.RegisterScreen
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.viewmodel.AuthViewModel

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
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomePage(
                userProfile = authState.userProfile,
                isAuthenticated = authState.isAuthenticated,
                onProfileClick = {
                    if (authState.isAuthenticated) {
                        navController.navigate("profile")
                    } else {
                        navController.navigate("register")
                    }
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
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("profileSetup") {
                        popUpTo("register") { inclusive = true }
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
                authViewModel = authViewModel,
                onLoginSuccess = {
                    // Check if profile is complete
                    if (authState.isNewUser || authState.userProfile?.isProfileComplete != true) {
                        navController.navigate("profileSetup") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onForgotPasswordClick = {
                    // TODO: Handle forgot password
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
        
        composable("profileSetup") {
            ProfileSetupScreen(
                isLoading = authState.isLoading,
                error = authState.error,
                onSaveProfile = { firstName, lastName, imageUri ->
                    authViewModel.saveUserProfile(firstName, lastName, imageUri)
                    // Navigate will happen automatically when profile is saved successfully
                }
            )
            
            // Watch for profile completion
            if (authState.userProfile?.isProfileComplete == true && !authState.isLoading) {
                navController.navigate("home") {
                    popUpTo("profileSetup") { inclusive = true }
                }
            }
        }
        
        composable("profile") {
            ProfileScreen(
                userProfile = authState.userProfile,
                isLoading = authState.isLoading,
                error = authState.error,
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdateProfile = { firstName, lastName, imageUri ->
                    authViewModel.updateUserProfile(firstName, lastName, imageUri)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}