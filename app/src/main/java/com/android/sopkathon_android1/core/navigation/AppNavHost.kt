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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.sopkathon_android1.feature.eunji.navigation.Eunji
import com.android.sopkathon_android1.feature.eunji.navigation.eunjiNavGraph
import com.android.sopkathon_android1.feature.heejin.navigation.Heejin
import com.android.sopkathon_android1.feature.heejin.navigation.heejinNavGraph
import com.android.sopkathon_android1.feature.hyemin.navigation.Hyemin
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

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<Eunji>() == true,
                    onClick = { navController.navigateToTab(Eunji) },
                    icon = { Text("은") },
                    label = { Text("은지") },
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<Hyemin>() == true,
                    onClick = { navController.navigateToTab(Hyemin) },
                    icon = { Text("혜") },
                    label = { Text("혜민") },
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<Heejin>() == true,
                    onClick = { navController.navigateToTab(Heejin) },
                    icon = { Text("희") },
                    label = { Text("희진") },
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<Seungyeon>() == true,
                    onClick = { navController.navigateToTab(Seungyeon) },
                    icon = { Text("승") },
                    label = { Text("승연") },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Main,
            modifier = Modifier.padding(innerPadding),
        ) {
            mainNavGraph()
            eunjiNavGraph()
            hyeminNavGraph()
            heejinNavGraph()
            seungyeonNavGraph()
        }
    }
}
