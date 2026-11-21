package com.android.sopkathon_android1.feature.hyemin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.android.sopkathon_android1.feature.hyemin.HyeminDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data class HyeminDetail(val missionId: String)

fun NavController.navigateToHyeminDetail(missionId: String, navOptions: NavOptions? = null) {
    navigate(HyeminDetail(missionId), navOptions)
}

fun NavGraphBuilder.hyeminDetailNavGraph(onBack: () -> Unit) {
    composable<HyeminDetail> {
        HyeminDetailRoute(onBack = onBack)
    }
}
