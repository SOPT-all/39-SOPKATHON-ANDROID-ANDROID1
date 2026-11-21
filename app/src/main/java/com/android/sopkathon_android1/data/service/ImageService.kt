package com.android.sopkathon_android1.data.service

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.image.UploadImageResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("v1/images")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): Response<BaseResponse<UploadImageResponseDto>>
}
