package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.data.model.HyeminMissionDetail

interface MissionBasketRepository {
    suspend fun getMissions(): List<HyeminMission>
    suspend fun getMissionDetail(missionId: Long): HyeminMissionDetail
}
