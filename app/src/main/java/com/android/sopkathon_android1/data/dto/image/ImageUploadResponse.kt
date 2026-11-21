package com.android.sopkathon_android1.data.dto.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponse(
    @SerialName("imageKey") val imageKey: String,
    @SerialName("imageUrl") val imageUrl: String,
)
