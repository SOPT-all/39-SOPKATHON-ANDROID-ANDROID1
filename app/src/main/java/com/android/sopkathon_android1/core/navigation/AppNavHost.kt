package com.android.sopkathon_android1.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.sopkathon_android1.feature.eunji.EunjiRoute
import com.android.sopkathon_android1.feature.eunji.EunjiScreen
import com.android.sopkathon_android1.feature.heejin.HeejinRoute
import com.android.sopkathon_android1.feature.heejin.HeejinScreen
import com.android.sopkathon_android1.feature.hyemin.HyeminRoute
import com.android.sopkathon_android1.feature.hyemin.HyeminScreen
import com.android.sopkathon_android1.feature.main.MainRoute
import com.android.sopkathon_android1.feature.main.MainScreen
import com.android.sopkathon_android1.feature.seungyeon.SeungyeonRoute
import com.android.sopkathon_android1.feature.seungyeon.SeungyeonScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoute,
        modifier = modifier,
    ) {
        composable<MainRoute> {
            MainScreen(
                onEunjiClick = { navController.navigate(EunjiRoute) },
                onHyeminClick = { navController.navigate(HyeminRoute) },
                onHeejinClick = { navController.navigate(HeejinRoute) },
                onSeungyeonClick = { navController.navigate(SeungyeonRoute) },
            )
        }
        composable<EunjiRoute> { EunjiScreen() }
        composable<HyeminRoute> { HyeminScreen() }
        composable<HeejinRoute> { HeejinScreen() }
        composable<SeungyeonRoute> { SeungyeonScreen() }
    }
}
