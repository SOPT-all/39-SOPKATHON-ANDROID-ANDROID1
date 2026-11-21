package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.MissionVerificationResult

interface MissionCompletionRepository {
    suspend fun completeMission(
        missionBasketId: Long,
        imageKey: String,
        content: String,
    ): MissionVerificationResult
}
