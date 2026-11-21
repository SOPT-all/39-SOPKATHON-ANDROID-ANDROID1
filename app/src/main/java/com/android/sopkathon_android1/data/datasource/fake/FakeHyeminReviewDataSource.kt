package com.android.sopkathon_android1.data.datasource.fake

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.hyemin.MissionReviewDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

@Singleton
class FakeHyeminReviewDataSource @Inject constructor() {
    var scenario = FakeHyeminScenario.SUCCESS
    private var reviews = listOf(
        MissionReviewDto(
            reviewId = 1,
            imageUrl = "https://example.com/reviews/1.jpg",
            title = "셀프 사진 네 컷 찍어보기",
            content = "처음엔 어색했는데 재밌었어요.",
            writerName = "민수",
            createdAt = "2026-11-22T14:30:00",
            likeCount = 4,
            isLiked = true,
        ),
        MissionReviewDto(
            reviewId = 2,
            imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_movie",
            title = "먼작귀 극장판 보기",
            content = "캐릭터가 귀엽고 신기하네요.",
            writerName = "정호",
            createdAt = "2026-11-21T18:30:00",
            likeCount = 17,
            isLiked = false,
        ),
        MissionReviewDto(
            reviewId = 3,
            imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_roll",
            title = "머랭롤 체험해보기",
            content = "너무 맛있었어요.",
            writerName = "영희",
            createdAt = "2026-11-20T12:00:00",
            likeCount = 8,
            isLiked = false,
        ),
    )

    suspend fun getReviews(): BaseResponse<List<MissionReviewDto>> {
        delay(500)
        return when (scenario) {
            FakeHyeminScenario.SUCCESS -> BaseResponse(0, "MOCK_SUCCESS", "Mock 조회 성공", reviews.sortedByDescending { it.createdAt })
            FakeHyeminScenario.EMPTY -> BaseResponse(0, "MOCK_SUCCESS", "Mock 조회 성공", emptyList())
            FakeHyeminScenario.ERROR -> BaseResponse(500, "INTERNAL_SERVER_ERROR", "오류가 발생했습니다. 다시 시도해 주세요.", null)
        }
    }

    // 좋아요 API 명세가 없으므로 Mock 상태만 변경한다.
    fun toggleLike(reviewId: Long): MissionReviewDto {
        val current = reviews.first { it.reviewId == reviewId }
        val updated = current.copy(
            isLiked = !current.isLiked,
            likeCount = current.likeCount + if (current.isLiked) -1 else 1,
        )
        reviews = reviews.map { if (it.reviewId == reviewId) updated else it }
        return updated
    }
}
