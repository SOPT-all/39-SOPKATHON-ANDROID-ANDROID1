package com.android.sopkathon_android1.feature.main

import com.android.sopkathon_android1.data.model.MainItem

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val items: List<MainItem>) : MainUiState
    data object Empty : MainUiState
    data object Error : MainUiState
}
