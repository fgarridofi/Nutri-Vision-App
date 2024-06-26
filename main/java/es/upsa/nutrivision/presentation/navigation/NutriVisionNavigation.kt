package es.upsa.nutrivision.presentation.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.presentation.screens.home.HomeScreen
import es.upsa.nutrivision.presentation.screens.home.HomeViewModel
import es.upsa.nutrivision.presentation.screens.scan.ScanFoodScreen
import es.upsa.nutrivision.presentation.screens.scan.ScanFoodViewModel
import es.upsa.nutrivision.presentation.screens.search.AddCustomFoodScreen
import es.upsa.nutrivision.presentation.screens.search.AddFoodScreen
import es.upsa.nutrivision.presentation.screens.search.SearchFoodScreen
import es.upsa.nutrivision.presentation.screens.search.SearchFoodViewModel
import es.upsa.nutrivision.presentation.screens.splash.SplashScreen
import es.upsa.nutrivision.presentation.screens.userdata.UserDataScreen
import es.upsa.nutrivision.presentation.screens.userdata.UserDataViewModel
import es.upsa.nutrivision.presentation.widgets.BottomNavigationBarComponent

@Composable
fun NutriVisionNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userDataViewModel = hiltViewModel<UserDataViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val searchFoodViewModel = hiltViewModel<SearchFoodViewModel>()
    val scanFoodViewModel = hiltViewModel<ScanFoodViewModel>()

    Scaffold(
        bottomBar = {
            if (currentRoute != NutriVisionScreens.SplashScreen.name) {
                BottomNavigationBarComponent(navController = navController)
            }

        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = NutriVisionScreens.SplashScreen.name,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(NutriVisionScreens.SplashScreen.name) {
                SplashScreen(navController, userDataViewModel)
            }
            composable(NutriVisionScreens.HomeScreen.name) {
                HomeScreen(navController, homeViewModel)
            }
            composable(NutriVisionScreens.UserDataScreen.name) {
                UserDataScreen(navController, userDataViewModel)
            }
            composable(NutriVisionScreens.AddCustomFoodScreen.name) {
                AddCustomFoodScreen(navController, homeViewModel)
            }
            composable(NutriVisionScreens.SearchFoodScreen.name) {
                SearchFoodScreen(navController, searchFoodViewModel)
            }
            composable(NutriVisionScreens.ScanFoodScreen.name) {
                scanFoodViewModel.resetState()
                ScanFoodScreen(navController, scanFoodViewModel)
            }

            composable(
                route = "addFood/{foodJson}",
                arguments = listOf(navArgument("foodJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val foodJson = backStackEntry.arguments?.getString("foodJson")
                val foodType = object : TypeToken<Food>() {}.type
                val food = Gson().fromJson<Food>(Uri.decode(foodJson), foodType)
                Log.d("NutriVisionNavigation", "food: $food")
                AddFoodScreen(navController, food, homeViewModel)
            }
        }
    }
}
