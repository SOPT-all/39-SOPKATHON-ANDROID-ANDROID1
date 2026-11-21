package com.android.sopkathon_android1.data.datasource.fake

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.hyemin.MissionBasketDto
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import com.android.sopkathon_android1.data.model.HyeminMissionDetail
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

enum class FakeHyeminScenario { SUCCESS, EMPTY, ERROR }

enum class FakeHyeminVerificationScenario { SUCCESS, FAILURE, ERROR }

@Singleton
class FakeHyeminDataSource @Inject constructor() {
    var scenario = FakeHyeminScenario.SUCCESS
    var verificationScenario = FakeHyeminVerificationScenario.SUCCESS
    private val missions = listOf(
        MissionBasketDto(
            missionBasketId = 12,
            missionId = 3,
            title = "셀프 사진 네 컷 찍어보기",
            imageUrl = "https://example.com/missions/3.jpg",
            createdAt = "2026-11-21T18:30:00",
        ),
        MissionBasketDto(
            missionBasketId = 13,
            missionId = 4,
            title = "먼작귀 극장판 보기",
            imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_movie",
            createdAt = "2026-11-20T18:30:00",
        ),
        MissionBasketDto(
            missionBasketId = 14,
            missionId = 5,
            title = "머랭롤 체험해보기",
            imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_roll",
            createdAt = "2026-11-19T18:30:00",
        ),
    )
    private val completedMissionIds = mutableSetOf(5L)

    suspend fun getMissionBaskets(): BaseResponse<List<MissionBasketDto>> {
        delay(500)
        if (scenario == FakeHyeminScenario.ERROR) error("Mock 조회 실패")
        // 실제 성공 code는 미확정이므로 Mock 전용 값을 사용한다.
        return BaseResponse(
            status = 0,
            code = "MOCK_SUCCESS",
            message = "Mock 조회 성공",
            data = if (scenario == FakeHyeminScenario.EMPTY) emptyList() else missions,
        )
    }

    fun isCompleted(missionId: Long): Boolean = missionId in completedMissionIds

    fun markCompleted(missionId: Long) {
        completedMissionIds.add(missionId)
    }

    // API 응답 구조를 참고한 Mock 값이며 실제 네트워크 요청은 하지 않는다.
    suspend fun getMissionDetail(missionId: Long): HyeminMissionDetail {
        delay(500)
        check(scenario != FakeHyeminScenario.ERROR) { "Mock 상세설명 조회 실패" }
        require(missions.any { it.missionId == missionId })
        return when (missionId) {
            3L -> HyeminMissionDetail(
                description = "좋아하는 표정으로 나만의 네 컷 사진을 남겨보세요.",
                instructions = "1. 셀프 사진관 또는 휴대폰 준비\n2. 좋아하는 표정으로 네 컷 촬영\n3. 아래에 사진과 후기를 등록",
            )
            4L -> HyeminMissionDetail(
                description = "먼작귀 극장판을 보고 새로운 즐거움을 발견해보세요.",
                instructions = "1. 관람할 상영 시간 확인\n2. 영화 관람\n3. 아래에 사진과 후기를 등록",
            )
            else -> HyeminMissionDetail(
                description = "머랭롤을 체험하고 후기를 남겨보세요.",
                instructions = "1. 머랭롤 준비\n2. 머랭롤 체험\n3. 아래에 사진과 후기를 등록",
            )
        }
    }

    suspend fun completeMission(id: String, photo: String, review: String): MissionVerificationResult {
        require(photo.isNotBlank() && review.isNotBlank())
        val missionId = id.toLong()
        require(missions.any { it.missionId == missionId })
        delay(500)
        check(scenario != FakeHyeminScenario.ERROR)
        return when (verificationScenario) {
            FakeHyeminVerificationScenario.SUCCESS -> {
                completedMissionIds.add(missionId)
                MissionVerificationResult.SUCCESS
            }
            FakeHyeminVerificationScenario.FAILURE -> MissionVerificationResult.FAILURE
            FakeHyeminVerificationScenario.ERROR -> error("Mock 인증 요청 실패")
        }
    }
}
