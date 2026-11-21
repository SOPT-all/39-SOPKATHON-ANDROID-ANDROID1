package com.android.sopkathon_android1.data.datasource.fake

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import com.android.sopkathon_android1.data.dto.hyemin.MissionBasketDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

enum class FakeHyeminScenario { SUCCESS, EMPTY, ERROR }

@Singleton
class FakeHyeminDataSource @Inject constructor() {
    var scenario = FakeHyeminScenario.SUCCESS
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

    suspend fun completeMission(id: String, photo: String, review: String) {
        require(photo.isNotBlank() && review.isNotBlank())
        val missionId = id.toLong()
        require(missions.any { it.missionId == missionId })
        delay(500)
        check(scenario != FakeHyeminScenario.ERROR)
        completedMissionIds.add(missionId)
    }
}
