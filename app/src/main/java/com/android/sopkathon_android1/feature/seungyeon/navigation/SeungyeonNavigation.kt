package com.android.sopkathon_android1.feature.seungyeon.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.seungyeon.SeungyeonRoute
import kotlinx.serialization.Serializable

@Serializable
data object Seungyeon

fun NavController.navigateToSeungyeon(navOptions: NavOptions? = null) {
    navigate(route = Seungyeon, navOptions = navOptions)
}

fun NavGraphBuilder.seungyeonNavGraph() {
    composable<Seungyeon> {
        SeungyeonRoute()
    }
}
