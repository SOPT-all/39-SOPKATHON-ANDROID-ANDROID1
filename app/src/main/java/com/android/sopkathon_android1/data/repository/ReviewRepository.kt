package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.HyeminReview

interface ReviewRepository {
    suspend fun getReviews(): List<HyeminReview>
    suspend fun toggleLike(reviewId: Long): ReviewLikeResult
}

data class ReviewLikeResult(
    val reviewId: Long,
    val isLiked: Boolean,
    val likeCount: Int,
)
