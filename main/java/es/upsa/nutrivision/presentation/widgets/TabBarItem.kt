package es.upsa.nutrivision.presentation.widgets

import androidx.navigation.ActivityNavigator

data class TabBarItem(
    val title: String,
    val destination: String,
    val selectedIcon: Any,
    val unselectedIcon: Any,
    val badgeAmount: Int? = null
)
