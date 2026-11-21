package com.android.sopkathon_android1.feature.heejin.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.android.sopkathon_android1.feature.heejin.HeejinRoute
import kotlinx.serialization.Serializable

@Serializable
data object Heejin

fun NavController.navigateToHeejin(navOptions: NavOptions? = null) {
    navigate(route = Heejin, navOptions = navOptions)
}

fun NavGraphBuilder.heejinNavGraph(
    onMissionCreated: (Long) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
) {
    composable<Heejin> {
        HeejinRoute(
            onMissionCreated = onMissionCreated,
            contentPadding = contentPadding,
        )
    }
}
