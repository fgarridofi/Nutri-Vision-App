package es.upsa.nutrivision.presentation.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController, currentRoute: String?) {
    NavigationBar {
        tabBarItems.forEach { tabBarItem ->
            val isSelected = currentRoute == tabBarItem.destination
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tabBarItem.destination) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = isSelected,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: Any,
    unselectedIcon: Any,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        val icon = if (isSelected) selectedIcon else unselectedIcon
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            is Painter -> Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            else -> throw IllegalArgumentException("Icon type not supported")
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge { Text(count.toString()) }
    }
}
