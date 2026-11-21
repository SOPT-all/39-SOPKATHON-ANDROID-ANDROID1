package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.dto.heejin.CreateMissionRequestDto

interface HeejinRepository {
    suspend fun uploadImage(photoUri: String): String

    suspend fun createMission(request: CreateMissionRequestDto): Long
}
