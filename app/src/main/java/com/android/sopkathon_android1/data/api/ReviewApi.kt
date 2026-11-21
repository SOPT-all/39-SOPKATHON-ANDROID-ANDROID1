package com.android.sopkathon_android1.data.api

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.hyemin.MissionReviewDto
import retrofit2.Response
import retrofit2.http.GET

interface ReviewApi {
    @GET("v1/reviews")
    suspend fun getReviews(): Response<BaseResponse<List<MissionReviewDto>>>
}
