package com.android.sopkathon_android1.data.dto.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadImageResponseDto(
    @SerialName("imageKey") val imageKey: String,
)
