package com.android.sopkathon_android1.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<EunjiRoute>() == true,
                    onClick = { navController.navigateToTab(EunjiRoute) },
                    icon = { Text("은") },
                    label = { Text("은지") },
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<HyeminRoute>() == true,
                    onClick = { navController.navigateToTab(HyeminRoute) },
                    icon = { Text("혜") },
                    label = { Text("혜민") },
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<HeejinRoute>() == true,
                    onClick = { navController.navigateToTab(HeejinRoute) },
                    icon = { Text("희") },
                    label = { Text("희진") },
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<SeungyeonRoute>() == true,
                    onClick = { navController.navigateToTab(SeungyeonRoute) },
                    icon = { Text("승") },
                    label = { Text("승연") },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<MainRoute> { MainScreen() }
            composable<EunjiRoute> { EunjiScreen() }
            composable<HyeminRoute> { HyeminScreen() }
            composable<HeejinRoute> { HeejinScreen() }
            composable<SeungyeonRoute> { SeungyeonScreen() }
        }
    }
}

private fun <T : Any> NavHostController.navigateToTab(route: T) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
