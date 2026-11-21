package com.android.sopkathon_android1.feature.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.main.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object Main

fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    navigate(route = Main, navOptions = navOptions)
}

fun NavGraphBuilder.mainNavGraph() {
    composable<Main> {
        MainScreen()
    }
}
