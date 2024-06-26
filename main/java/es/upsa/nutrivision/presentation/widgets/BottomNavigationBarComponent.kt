package es.upsa.nutrivision.presentation.widgets


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import es.upsa.nutrivision.R
import es.upsa.nutrivision.presentation.navigation.NutriVisionScreens

@Composable
fun BottomNavigationBarComponent(navController: NavController) {
    val items = listOf(
        TabBarItem("Home", NutriVisionScreens.HomeScreen.name, Icons.Filled.Home, Icons.Outlined.Home),
        TabBarItem("Scan Food", NutriVisionScreens.ScanFoodScreen.name, painterResource(id = R.drawable.barcode), painterResource(id = R.drawable.barcode)),
        TabBarItem("Search Food", NutriVisionScreens.SearchFoodScreen.name, Icons.Filled.Search, Icons.Outlined.Search),
        TabBarItem("Profile", NutriVisionScreens.UserDataScreen.name, Icons.Filled.Person, Icons.Outlined.Person),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TabView(
        tabBarItems = items,
        navController = navController,
        currentRoute = currentRoute
    )
}


