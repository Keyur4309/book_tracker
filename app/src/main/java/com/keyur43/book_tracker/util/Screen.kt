package com.keyur43.book_tracker.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Home : Screen("home", "History", Icons.Default.List)
    object Stats: Screen("stats", "Analytics", Icons.Default.Info)
    object Recent: Screen("recent", "Home", Icons.Default.Home)
    object BookDetail : Screen("detail", "Book Details", null)
}
