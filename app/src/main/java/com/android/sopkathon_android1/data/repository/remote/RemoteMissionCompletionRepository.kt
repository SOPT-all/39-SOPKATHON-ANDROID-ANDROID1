package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.core.network.errorCodeOrNull
import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.dto.hyemin.MissionCompletionRequest
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import com.android.sopkathon_android1.data.repository.MissionCompletionRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.serialization.json.Json

class RemoteMissionCompletionRepository @Inject constructor(
    private val api: MissionApi,
    private val json: Json,
) : MissionCompletionRepository {
    override suspend fun completeMission(
        missionBasketId: Long,
        imageKey: String,
        content: String,
    ): MissionVerificationResult {
        val response = api.completeMission(missionBasketId, MissionCompletionRequest(imageKey, content))
        if (!response.isSuccessful) {
            throw MissionCompletionException(response.code(), response.errorCodeOrNull(json))
        }
        return when (response.body()?.data?.verificationResult) {
            "PASS" -> MissionVerificationResult.SUCCESS
            "FAIL" -> MissionVerificationResult.FAILURE
            else -> throw MissionCompletionException(response.code(), response.body()?.code)
        }
    }
}

class MissionCompletionException(
    val httpStatus: Int,
    val code: String?,
) : IOException("미션 완료 실패 (HTTP $httpStatus, code=$code)")
