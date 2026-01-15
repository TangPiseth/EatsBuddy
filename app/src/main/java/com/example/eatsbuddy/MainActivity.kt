package com.example.eatsbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eatsbuddy.ui.components.BottomNavigationBar
import com.example.eatsbuddy.ui.screens.AboutPage
import com.example.eatsbuddy.ui.screens.ApiRecipeDetailsPage
import com.example.eatsbuddy.ui.screens.ApiRecipesPage
import com.example.eatsbuddy.ui.screens.ContactPage
import com.example.eatsbuddy.ui.screens.FAQPage
import com.example.eatsbuddy.ui.screens.GroceryListPage
import com.example.eatsbuddy.ui.screens.HomePage
import com.example.eatsbuddy.ui.screens.LegalsPage
import com.example.eatsbuddy.ui.screens.LoginScreen
import com.example.eatsbuddy.ui.screens.MealPlannerPage
import com.example.eatsbuddy.ui.screens.MorePage
import com.example.eatsbuddy.ui.screens.ProfileScreen
import com.example.eatsbuddy.ui.screens.ProfileSetupScreen
import com.example.eatsbuddy.ui.screens.RegisterScreen
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.viewmodel.AuthViewModel
import com.example.eatsbuddy.viewmodel.GroceryViewModel
import com.example.eatsbuddy.viewmodel.RecipeViewModel
import com.example.eatsbuddy.viewmodel.ThemeMode
import com.example.eatsbuddy.viewmodel.ThemeViewModel
import com.example.eatsbuddy.viewmodel.UserDataViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val themeMode by themeViewModel.themeMode.collectAsState()
            
            val isDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            
            EatsBuddyTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EatsBuddyApp(themeViewModel = themeViewModel)
                }
            }
        }
    }
}

@Composable
fun EatsBuddyApp(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val recipeViewModel: RecipeViewModel = viewModel()
    val groceryViewModel: GroceryViewModel = viewModel()
    val userDataViewModel: UserDataViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val homeState by recipeViewModel.homeState.collectAsState()
    val favoritesState by recipeViewModel.favoritesState.collectAsState()
    val themeMode by themeViewModel.themeMode.collectAsState()
    val groceryState by groceryViewModel.uiState.collectAsState()
    val userDataState by userDataViewModel.state.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomePage(
                userProfile = authState.userProfile,
                isAuthenticated = authState.isAuthenticated,
                popularMeals = homeState.popularMeals,
                categories = homeState.categories,
                isLoadingMeals = homeState.isLoading,
                onProfileClick = {
                    if (authState.isAuthenticated) {
                        navController.navigate("profile")
                    } else {
                        navController.navigate("register")
                    }
                },
                onRecipesClick = {
                    navController.navigate("recipes")
                },
                onMealPlannerClick = {
                    navController.navigate("mealPlanner")
                },
                onGroceryListClick = {
                    navController.navigate("groceryList")
                },
                onSearchClick = {
                    navController.navigate("recipes")
                },
                onRecipeClick = { recipeId ->
                    navController.navigate("recipeDetails/$recipeId")
                },
                onCategoryClick = { category ->
                    navController.navigate("recipes?category=$category")
                },
                onFavoriteClick = { mealId ->
                    // Get the meal preview for Firebase storage
                    val meal = homeState.popularMeals.find { it.id == mealId }
                    if (meal != null) {
                        userDataViewModel.toggleFavorite(meal)
                    }
                    recipeViewModel.toggleFavorite(mealId)
                },
                currentRoute = "home",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable("mealPlanner") {
            MealPlannerPage(
                userDataViewModel = userDataViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                currentRoute = "mealPlanner",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable("groceryList") {
            GroceryListPage(
                userDataViewModel = userDataViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                currentRoute = "groceryList",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable("recipes") {
            ApiRecipesPage(
                viewModel = recipeViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onRecipeClick = { mealId ->
                    navController.navigate("recipeDetails/$mealId")
                },
                currentRoute = "recipes",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable("recipes?category={category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            ApiRecipesPage(
                viewModel = recipeViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onRecipeClick = { mealId ->
                    navController.navigate("recipeDetails/$mealId")
                },
                initialCategory = category,
                currentRoute = "recipes",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable("recipeDetails/{recipeId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("recipeId") ?: ""
            ApiRecipeDetailsPage(
                mealId = mealId,
                viewModel = recipeViewModel,
                userDataViewModel = userDataViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddToGroceryList = { ingredients ->
                    // Get the meal name for tracking
                    val mealName = recipeViewModel.detailState.value.meal?.name ?: "Recipe"
                    userDataViewModel.addIngredientsFromRecipe(ingredients, mealName)
                    navController.navigate("groceryList")
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
                favoriteMeals = userDataState.favorites,
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
                },
                onFavoriteRecipeClick = { mealId ->
                    navController.navigate("recipeDetails/$mealId")
                }
            )
        }
        
        composable("more") {
            MorePage(
                currentRoute = "more",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onMenuItemClick = { route ->
                    navController.navigate(route)
                },
                isDarkMode = themeMode == ThemeMode.DARK,
                onToggleDarkMode = { themeViewModel.toggleTheme() }
            )
        }
        
        composable("faq") {
            FAQPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("legals") {
            LegalsPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("contact") {
            ContactPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("about") {
            AboutPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}