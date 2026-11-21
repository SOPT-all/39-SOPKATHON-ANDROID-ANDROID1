package com.android.sopkathon_android1.data.dto.hyemin

import kotlinx.serialization.Serializable

@Serializable
data class ReviewLikeResponseDto(
    val reviewId: Long,
    val isLiked: Boolean,
    val likeCount: Int,
)
