package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.HyeminMission

interface MissionBasketRepository {
    suspend fun getMissions(): List<HyeminMission>
}
