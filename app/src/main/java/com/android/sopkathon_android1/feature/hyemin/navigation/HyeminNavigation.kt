package com.android.sopkathon_android1.feature.hyemin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.hyemin.HyeminRoute
import kotlinx.serialization.Serializable

@Serializable
data object Hyemin

fun NavController.navigateToHyemin(navOptions: NavOptions? = null) {
    navigate(route = Hyemin, navOptions = navOptions)
}

fun NavGraphBuilder.hyeminNavGraph() {
    composable<Hyemin> {
        HyeminRoute()
    }
}
