package com.android.sopkathon_android1.data.dto.heejin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMissionRequestDto(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("instructions") val instructions: String,
    @SerialName("imageKey") val imageKey: String,
)
