package com.android.sopkathon_android1.feature.hyemin

import com.android.sopkathon_android1.data.model.HyeminMission

data class HyeminDetailUiState(
    val mission: HyeminMission? = null,
    val loading: Boolean = true,
    val photo: String? = null,
    val liked: Boolean = false,
    val submitting: Boolean = false,
    val finished: Boolean = false,
    val message: String? = null,
)
