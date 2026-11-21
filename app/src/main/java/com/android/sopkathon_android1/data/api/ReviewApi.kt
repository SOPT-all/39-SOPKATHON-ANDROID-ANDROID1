package com.android.sopkathon_android1.data.api

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.hyemin.MissionReviewDto
import com.android.sopkathon_android1.data.dto.hyemin.ReviewLikeResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {
    @GET("v1/reviews")
    suspend fun getReviews(): Response<BaseResponse<List<MissionReviewDto>>>

    @POST("v1/reviews/{reviewId}/likes")
    suspend fun toggleLike(
        @Path("reviewId") reviewId: Long,
    ): Response<BaseResponse<ReviewLikeResponseDto>>
}
