package com.android.sopkathon_android1.data.service

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.heejin.CreateMissionRequestDto
import com.android.sopkathon_android1.data.dto.heejin.CreateMissionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface HeejinService {
    @POST("v1/users/{userId}/missions")
    suspend fun createMission(
        @Path("userId") userId: Long,
        @Body request: CreateMissionRequestDto,
    ): Response<BaseResponse<CreateMissionResponseDto>>
}
