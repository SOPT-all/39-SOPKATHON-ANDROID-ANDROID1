package com.android.sopkathon_android1.data.api

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.request.MissionBasketCreateRequestDto
import com.android.sopkathon_android1.data.dto.response.MissionBasketCreateResponseDto
import com.android.sopkathon_android1.data.dto.response.MissionDetailResponseDto
import com.android.sopkathon_android1.data.dto.response.MissionListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MissionApi {
    @GET("v1/missions")
    suspend fun findMissions(): BaseResponse<List<MissionListResponseDto>>

    @GET("v1/missions/{missionId}")
    suspend fun getMissionDetail(
        @Path("missionId") missionId: Long,
    ): BaseResponse<MissionDetailResponseDto>

    @POST("v1/users/{userId}/mission-baskets")
    suspend fun addMissionToBasket(
        @Path("userId") userId: Long,
        @Body request: MissionBasketCreateRequestDto,
    ): BaseResponse<MissionBasketCreateResponseDto>
}
