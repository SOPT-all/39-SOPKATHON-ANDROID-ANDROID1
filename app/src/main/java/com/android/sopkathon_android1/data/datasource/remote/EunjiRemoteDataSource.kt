package com.android.sopkathon_android1.data.datasource.remote

import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.request.MissionBasketCreateRequestDto
import com.android.sopkathon_android1.data.dto.response.MissionBasketCreateResponseDto
import com.android.sopkathon_android1.data.dto.response.MissionDetailResponseDto
import com.android.sopkathon_android1.data.dto.response.MissionListResponseDto
import javax.inject.Inject

class EunjiRemoteDataSource @Inject constructor(
    private val missionApi: MissionApi,
) {
    suspend fun findMissions(): BaseResponse<List<MissionListResponseDto>> =
        missionApi.findMissions()

    suspend fun getMissionDetail(missionId: Long): BaseResponse<MissionDetailResponseDto> =
        missionApi.getMissionDetail(missionId = missionId)

    suspend fun addMissionToBasket(
        userId: Long,
        missionId: Long,
    ): BaseResponse<MissionBasketCreateResponseDto> =
        missionApi.addMissionToBasket(
            userId = userId,
            request = MissionBasketCreateRequestDto(missionId = missionId),
        )
}
