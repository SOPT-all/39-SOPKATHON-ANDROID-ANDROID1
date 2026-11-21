package com.android.sopkathon_android1.data.dto.hyemin

import kotlinx.serialization.Serializable

/** GET /v1/reviews 응답의 data 항목. */
@Serializable
data class MissionReviewDto(
    val reviewId: Long,
    val imageUrl: String,
    val title: String,
    val content: String,
    val writerName: String,
    val createdAt: String,
    val likeCount: Int,
    val isLiked: Boolean,
)
