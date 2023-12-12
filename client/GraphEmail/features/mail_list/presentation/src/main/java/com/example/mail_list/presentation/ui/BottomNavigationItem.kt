package com.example.mail_list.presentation.ui

import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavigationItem (
    val screen: BottomNavScreens,
    val icon: ImageVector,
    val selected: Boolean
)

enum class BottomNavScreens(val title: String) {
    GRAPH("Graph"), MAIL_LIST("Mail list"), SETTINGS("Settings");
}