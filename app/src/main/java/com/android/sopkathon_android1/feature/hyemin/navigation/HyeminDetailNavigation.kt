package com.android.sopkathon_android1.feature.hyemin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.hyemin.HyeminDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data class HyeminDetail(val missionBasketId: String)

fun NavController.navigateToHyeminDetail(missionBasketId: String, navOptions: NavOptions? = null) {
    navigate(HyeminDetail(missionBasketId), navOptions)
}

fun NavGraphBuilder.hyeminDetailNavGraph(onBack: () -> Unit) {
    composable<HyeminDetail> {
        HyeminDetailRoute(onBack = onBack)
    }
}
