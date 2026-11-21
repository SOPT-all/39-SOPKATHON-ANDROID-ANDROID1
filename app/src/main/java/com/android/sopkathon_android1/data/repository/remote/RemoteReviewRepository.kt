package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.core.network.errorCodeOrNull
import com.android.sopkathon_android1.data.api.ReviewApi
import com.android.sopkathon_android1.data.model.HyeminReview
import com.android.sopkathon_android1.data.repository.ReviewRepository
import com.android.sopkathon_android1.data.repository.ReviewLikeResult
import java.io.IOException
import javax.inject.Inject
import kotlinx.serialization.json.Json

class RemoteReviewRepository @Inject constructor(
    private val api: ReviewApi,
    private val json: Json,
) : ReviewRepository {
    override suspend fun getReviews(): List<HyeminReview> {
        val response = api.getReviews()
        if (!response.isSuccessful) throw ReviewListException(response.code(), response.errorCodeOrNull(json))
        val reviews = response.body()?.data
            ?: throw ReviewListException(response.code(), response.body()?.code)
        return reviews.map { review ->
            HyeminReview(
                reviewId = review.reviewId,
                imageUrl = review.imageUrl,
                title = review.title,
                content = review.content,
                writerName = review.writerName,
                createdAt = review.createdAt,
                likeCount = review.likeCount,
                isLiked = review.isLiked,
            )
        }
    }

    override suspend fun toggleLike(reviewId: Long): ReviewLikeResult {
        val response = api.toggleLike(reviewId)
        if (!response.isSuccessful) throw ReviewLikeException(response.code(), response.errorCodeOrNull(json))
        val result = response.body()?.data
            ?: throw ReviewLikeException(response.code(), response.body()?.code)
        if (result.reviewId != reviewId) throw ReviewLikeException(response.code(), response.body()?.code)
        return ReviewLikeResult(result.reviewId, result.isLiked, result.likeCount)
    }
}

class ReviewListException(
    val httpStatus: Int,
    val code: String?,
) : IOException("인증글 목록 조회 실패 (HTTP $httpStatus, code=$code)")

class ReviewLikeException(
    val httpStatus: Int,
    val code: String?,
) : IOException("인증글 좋아요 변경 실패 (HTTP $httpStatus, code=$code)")
