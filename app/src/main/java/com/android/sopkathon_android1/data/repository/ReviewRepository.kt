package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.HyeminReview

interface ReviewRepository {
    suspend fun getReviews(): List<HyeminReview>
}
