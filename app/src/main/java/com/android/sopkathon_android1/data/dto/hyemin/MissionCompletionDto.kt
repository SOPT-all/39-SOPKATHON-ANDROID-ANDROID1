package com.android.sopkathon_android1.data.dto.hyemin

import kotlinx.serialization.Serializable

@Serializable
data class MissionCompletionRequest(
    val imageKey: String,
    val content: String,
)

@Serializable
data class MissionCompletionResponse(
    val verificationResult: String,
)
