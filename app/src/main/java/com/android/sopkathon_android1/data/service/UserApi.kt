package com.android.sopkathon_android1.data.service

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.response.UserDetailResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("v1/users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Long,
    ): Response<BaseResponse<UserDetailResponseDto>>
}
