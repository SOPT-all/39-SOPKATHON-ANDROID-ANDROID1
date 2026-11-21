package com.android.sopkathon_android1.feature.eunji

import com.android.sopkathon_android1.data.model.EunjiItem

sealed interface EunjiUiState {
    data object Loading : EunjiUiState
    data class Success(val items: List<EunjiItem>) : EunjiUiState
    data object Empty : EunjiUiState
    data object Error : EunjiUiState
}
