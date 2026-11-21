package com.android.sopkathon_android1.feature.seungyeon

import com.android.sopkathon_android1.feature.seungyeon.model.Badge

sealed interface SeungyeonUiState {
    data object Loading : SeungyeonUiState
    data class Success(val profile: MyPageUiModel) : SeungyeonUiState
    data class Error(val message: String) : SeungyeonUiState
}

data class MyPageUiModel(
    val name: String,
    val ageGroup: String,
    val profileImageUrl: String?,
    val completedMissionCount: Long,
    val receivedLikeCount: Long,
    val badge: Badge,
)
