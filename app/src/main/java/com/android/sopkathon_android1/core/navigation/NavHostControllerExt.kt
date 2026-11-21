package com.android.sopkathon_android1.core.navigation

import androidx.navigation.NavHostController

fun <T : Any> NavHostController.navigateToTab(route: T) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
