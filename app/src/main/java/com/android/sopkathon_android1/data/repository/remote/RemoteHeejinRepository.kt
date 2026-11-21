package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.core.network.errorCodeOrNull
import com.android.sopkathon_android1.data.datasource.ImageDataSource
import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.heejin.CreateMissionRequestDto
import com.android.sopkathon_android1.data.repository.HeejinApiException
import com.android.sopkathon_android1.data.repository.HeejinRepository
import com.android.sopkathon_android1.data.service.HeejinService
import com.android.sopkathon_android1.data.service.ImageService
import java.io.IOException
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.Response

private const val USER_ID = 1L

class RemoteHeejinRepository @Inject constructor(
    private val heejinService: HeejinService,
    private val imageService: ImageService,
    private val json: Json,
    private val imageDataSource: ImageDataSource,
) : HeejinRepository {
    override suspend fun uploadImage(photoUri: String): String {
        val file = imageDataSource.createImagePart(photoUri)
        val imageKey = imageService.uploadImage(file).requireData().imageKey
        if (imageKey.isBlank()) throw IOException("Image upload response has an empty imageKey")
        return imageKey
    }

    override suspend fun createMission(request: CreateMissionRequestDto): Long {
        return heejinService.createMission(userId = USER_ID, request = request)
            .requireData(expectedCode = "MISSION_CREATED")
            .missionId
    }

    private fun <T> Response<BaseResponse<T>>.requireData(expectedCode: String? = null): T {
        if (!isSuccessful) throw HeejinApiException(code(), errorCodeOrNull(json))
        val response = body() ?: throw IOException("API response body is missing")
        if (response.status !in 200..299 || (expectedCode != null && response.code != expectedCode)) {
            throw HeejinApiException(response.status, response.code)
        }
        return response.data ?: throw IOException("API response data is missing")
    }
}
