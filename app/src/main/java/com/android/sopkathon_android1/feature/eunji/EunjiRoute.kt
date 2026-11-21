package com.android.sopkathon_android1.feature.eunji

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable

@Serializable
data object EunjiDestination

@Composable
fun EunjiRoute(viewModel: EunjiViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EunjiScreen(uiState = uiState, onRetry = viewModel::loadItems)
}
