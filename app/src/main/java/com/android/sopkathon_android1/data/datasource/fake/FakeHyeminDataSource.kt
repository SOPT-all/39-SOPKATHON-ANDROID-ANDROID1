package com.android.sopkathon_android1.data.datasource.fake

import com.android.sopkathon_android1.data.model.HyeminMission
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

enum class FakeHyeminScenario { SUCCESS, EMPTY, ERROR }

@Singleton
class FakeHyeminDataSource @Inject constructor() {
    var scenario = FakeHyeminScenario.SUCCESS
    private var missions = listOf(
        HyeminMission("fruit", "집에서 한정선 만들기", "민수 50대 “처음엔 어색했는데 재밌었어요.”", instructions = "1. 좋아하는 과일과 재료 준비\n2. 과일을 넣어 한정선 만들기\n3. 아래에 사진과 후기를 등록", imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_fruit"),
        HyeminMission("movie", "먼작귀 극장판 보기", "정호 50대 “캐릭터가 귀엽고 신기하네요.”", "11.02.MON", instructions = "1. 보고 싶은 상영 시간 확인\n2. 먼작귀 극장판 관람\n3. 아래에 사진과 후기를 등록", imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_movie"),
        HyeminMission("roll", "머랭롤 체험해보기", "영희 50대 “너무 맛있었어요”", completed = true, instructions = "1. 머랭롤 준비\n2. 머랭롤 체험하기\n3. 아래에 사진과 후기를 등록", imageUrl = "android.resource://com.android.sopkathon_android1/drawable/img_mission_roll"),
    )

    suspend fun getMissions(): List<HyeminMission> {
        delay(500)
        return when (scenario) {
            FakeHyeminScenario.SUCCESS -> missions.toList()
            FakeHyeminScenario.EMPTY -> emptyList()
            FakeHyeminScenario.ERROR -> error("Mock 조회 실패")
        }
    }

    suspend fun completeMission(id: String, photo: String, review: String) {
        require(photo.isNotBlank() && review.isNotBlank())
        delay(500)
        check(scenario != FakeHyeminScenario.ERROR)
        missions = missions.map { if (it.id == id) it.copy(completed = true) else it }
    }
}
