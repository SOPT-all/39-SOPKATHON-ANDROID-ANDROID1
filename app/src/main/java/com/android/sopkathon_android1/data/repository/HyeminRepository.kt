package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.HyeminMissionDraft
import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.data.model.HyeminReview

interface HyeminRepository {
    suspend fun getReviews(): List<HyeminReview>
    suspend fun toggleReviewLike(reviewId: Long): HyeminReview
    suspend fun getDraft(id: String): HyeminMissionDraft?
    suspend fun saveDraft(id: String, draft: HyeminMissionDraft)
    suspend fun getMissions(): List<HyeminMission>
    suspend fun completeMission(id: String, photo: String, review: String, liked: Boolean)
}
