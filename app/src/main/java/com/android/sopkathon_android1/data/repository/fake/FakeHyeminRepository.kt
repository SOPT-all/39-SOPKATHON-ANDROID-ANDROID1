package com.android.sopkathon_android1.data.repository.fake

import com.android.sopkathon_android1.data.model.HyeminMissionDraft
import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.data.model.HyeminMissionDetail
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminReviewDataSource
import com.android.sopkathon_android1.data.dto.hyemin.MissionReviewDto
import com.android.sopkathon_android1.data.model.HyeminReview
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import com.android.sopkathon_android1.data.repository.HyeminRepository
import javax.inject.Inject

class FakeHyeminRepository @Inject constructor(
    private val dataSource: FakeHyeminDataSource,
    private val reviewDataSource: FakeHyeminReviewDataSource,
) : HyeminRepository {
    override suspend fun getMissionDetail(missionId: Long): HyeminMissionDetail =
        dataSource.getMissionDetail(missionId)

    override suspend fun getReviews(): List<HyeminReview> {
        val response = reviewDataSource.getReviews()
        check(response.code == "MOCK_SUCCESS") { "인증글 조회 실패: ${response.code}" }
        return checkNotNull(response.data) { "인증글 응답 data가 없습니다" }.map { it.toModel() }
    }

    override suspend fun toggleReviewLike(reviewId: Long): HyeminReview =
        reviewDataSource.toggleLike(reviewId).toModel()

    private val drafts = mutableMapOf<String, HyeminMissionDraft>()
    override suspend fun getDraft(id: String) = drafts[id]
    override suspend fun saveDraft(id: String, draft: HyeminMissionDraft) {
        drafts[id] = draft
    }
    override suspend fun getMissions(): List<HyeminMission> {
        val response = dataSource.getMissionBaskets()
        check(response.code == "MOCK_SUCCESS") { "Mock 조회 실패: ${response.code}" }
        return checkNotNull(response.data) { "목록 응답 data가 없습니다" }.map { dto ->
            HyeminMission(
                missionBasketId = dto.missionBasketId,
                missionId = dto.missionId,
                title = dto.title,
                imageUrl = dto.imageUrl,
                createdAt = dto.createdAt,
                completed = dataSource.isCompleted(dto.missionId),
            )
        }
    }
    override suspend fun completeMission(id: String, photo: String, review: String, liked: Boolean): MissionVerificationResult {
        val result = dataSource.completeMission(id, photo, review)
        if (result == MissionVerificationResult.SUCCESS) drafts.remove(id)
        return result
    }

    override fun markCompleted(missionId: Long) {
        dataSource.markCompleted(missionId)
    }
}

private fun MissionReviewDto.toModel() = HyeminReview(
    reviewId = reviewId,
    imageUrl = imageUrl,
    title = title,
    content = content,
    writerName = writerName,
    createdAt = createdAt,
    likeCount = likeCount,
    isLiked = isLiked,
)
