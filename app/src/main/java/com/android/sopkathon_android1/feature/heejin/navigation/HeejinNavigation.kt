package com.android.sopkathon_android1.feature.heejin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.heejin.HeejinRoute
import kotlinx.serialization.Serializable

@Serializable
data object Heejin

fun NavController.navigateToHeejin(navOptions: NavOptions? = null) {
    navigate(route = Heejin, navOptions = navOptions)
}

fun NavGraphBuilder.heejinNavGraph() {
    composable<Heejin> {
        HeejinRoute()
    }
}
