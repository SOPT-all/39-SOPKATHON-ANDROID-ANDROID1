package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.core.network.errorCodeOrNull
import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.hyemin.MissionBasketDto
import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.data.repository.MissionBasketRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.Response

class RemoteMissionBasketRepository @Inject constructor(
    private val api: MissionApi,
    private val json: Json,
) : MissionBasketRepository {
    override suspend fun getMissions(): List<HyeminMission> {
        val incomplete = api.getIncompleteMissions(USER_ID).requireData(json)
            .map { it.toModel(completed = false) }
        val completed = api.getCompletedMissions(USER_ID).requireData(json)
            .map { it.toModel(completed = true) }
        return (incomplete + completed).sortedByDescending { it.createdAt }
    }

    private fun Response<BaseResponse<List<MissionBasketDto>>>.requireData(json: Json): List<MissionBasketDto> {
        if (!isSuccessful) throw MissionBasketException(code(), errorCodeOrNull(json))
        return body()?.data ?: throw MissionBasketException(code(), body()?.code)
    }

    private fun MissionBasketDto.toModel(completed: Boolean) = HyeminMission(
        missionBasketId = missionBasketId,
        missionId = missionId,
        title = title,
        imageUrl = imageUrl,
        createdAt = createdAt,
        completed = completed,
    )

    private companion object {
        const val USER_ID = 1L
    }
}

class MissionBasketException(
    val httpStatus: Int,
    val code: String?,
) : IOException("미션 목록 조회 실패 (HTTP $httpStatus, code=$code)")
