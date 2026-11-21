package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.core.network.errorCodeOrNull
import com.android.sopkathon_android1.data.dto.response.UserDetailResponseDto
import com.android.sopkathon_android1.data.service.UserApi
import javax.inject.Inject
import kotlinx.serialization.json.Json

class ApiException(val code: String?) : Exception("API error: $code")

interface UserRepository {
    suspend fun getUserDetail(userId: Long): UserDetailResponseDto
}

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val json: Json,
) : UserRepository {
    override suspend fun getUserDetail(userId: Long): UserDetailResponseDto {
        val response = userApi.getUserDetail(userId)
        if (!response.isSuccessful) throw ApiException(response.errorCodeOrNull(json))
        return response.body()?.data ?: throw ApiException(code = null)
    }
}
