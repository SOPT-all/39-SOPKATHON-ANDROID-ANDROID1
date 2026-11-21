package com.android.sopkathon_android1.data.model

data class HyeminReview(
    val reviewId: Long,
    val imageUrl: String,
    val title: String,
    val content: String,
    val writerName: String,
    val createdAt: String,
    val likeCount: Int,
    val isLiked: Boolean,
)
