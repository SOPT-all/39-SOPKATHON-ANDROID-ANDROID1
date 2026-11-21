package com.android.sopkathon_android1.feature.eunji.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.eunji.EunjiRoute
import kotlinx.serialization.Serializable

@Serializable
data object Eunji

fun NavController.navigateToEunji(navOptions: NavOptions? = null) {
    navigate(route = Eunji, navOptions = navOptions)
}

fun NavGraphBuilder.eunjiNavGraph() {
    composable<Eunji> {
        EunjiRoute()
    }
}
