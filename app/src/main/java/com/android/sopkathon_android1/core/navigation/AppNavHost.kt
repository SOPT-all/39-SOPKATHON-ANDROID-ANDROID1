package com.android.sopkathon_android1.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.android.sopkathon_android1.core.designsystem.component.NolItBottomNavigationBar
import com.android.sopkathon_android1.core.designsystem.component.NolItBottomTab
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.feature.eunji.navigation.Eunji
import com.android.sopkathon_android1.feature.eunji.navigation.eunjiNavGraph
import com.android.sopkathon_android1.feature.heejin.navigation.Heejin
import com.android.sopkathon_android1.feature.heejin.navigation.heejinNavGraph
import com.android.sopkathon_android1.feature.hyemin.navigation.Hyemin
import com.android.sopkathon_android1.feature.hyemin.navigation.HyeminDetail
import com.android.sopkathon_android1.feature.hyemin.navigation.hyeminDetailNavGraph
import com.android.sopkathon_android1.feature.hyemin.navigation.navigateToHyeminDetail
import com.android.sopkathon_android1.feature.hyemin.navigation.hyeminNavGraph
import com.android.sopkathon_android1.feature.main.navigation.Main
import com.android.sopkathon_android1.feature.main.navigation.mainNavGraph
import com.android.sopkathon_android1.feature.seungyeon.navigation.Seungyeon
import com.android.sopkathon_android1.feature.seungyeon.navigation.seungyeonNavGraph

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val selectedTab = when {
        currentDestination?.hasRoute<Eunji>() == true -> NolItBottomTab.HOME
        currentDestination?.hasRoute<Hyemin>() == true || currentDestination?.hasRoute<HyeminDetail>() == true -> NolItBottomTab.CHALLENGE
        currentDestination?.hasRoute<Heejin>() == true -> NolItBottomTab.CREATE
        currentDestination?.hasRoute<Seungyeon>() == true -> NolItBottomTab.MY
        else -> null
    }

    Scaffold(
        modifier = modifier,
        containerColor = NolItTheme.colors.background,
        bottomBar = {
            NolItBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    when (tab) {
                        NolItBottomTab.HOME -> navController.navigateToTab(Eunji)
                        NolItBottomTab.CHALLENGE -> navController.navigateToTab(Hyemin)
                        NolItBottomTab.CREATE -> navController.navigateToTab(Heejin)
                        NolItBottomTab.MY -> navController.navigateToTab(Seungyeon)
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Main,
            modifier = Modifier.padding(innerPadding),
        ) {
            mainNavGraph()
            eunjiNavGraph()
            hyeminNavGraph(onMissionClick = { navController.navigateToHyeminDetail(it) })
            hyeminDetailNavGraph(onBack = { navController.popBackStack() })
            heejinNavGraph(contentPadding = innerPadding)
            seungyeonNavGraph()
        }
    }
}
