package com.android.sopkathon_android1.feature.hyemin

import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminReviewDataSource
import com.android.sopkathon_android1.core.designsystem.component.ChallengeTab
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminScenario
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
import com.android.sopkathon_android1.data.repository.MissionBasketRepository
import com.android.sopkathon_android1.data.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HyeminViewModelTest {
    @Before fun setup() { Dispatchers.setMain(StandardTestDispatcher()) }
    @After fun cleanup() { Dispatchers.resetMain() }

    private fun viewModel(repository: FakeHyeminRepository) = HyeminViewModel(
        repository,
        object : MissionBasketRepository {
            override suspend fun getMissions() = repository.getMissions()
            override suspend fun getMissionDetail(missionId: Long) = repository.getMissionDetail(missionId)
        },
        object : ReviewRepository {
            override suspend fun getReviews() = repository.getReviews()
        },
    )

    @Test fun `조회 로딩 이후 미완료와 완료 미션을 제공한다`() = runTest {
        val vm = viewModel(FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource()))
        runCurrent()
        assertTrue(vm.uiState.value.loading)
        advanceUntilIdle()
        assertEquals(2, vm.uiState.value.missions.count { !it.completed })
        assertEquals(1, vm.uiState.value.missions.count { it.completed })
    }

    @Test fun `응답의 두 식별자와 화면 표시 필드를 유지한다`() = runTest {
        val source = FakeHyeminDataSource()
        val response = source.getMissionBaskets()
        val json = kotlinx.serialization.json.Json
        val decoded = json.decodeFromString(
            com.android.sopkathon_android1.data.dto.base.BaseResponse.serializer(
                kotlinx.serialization.builtins.ListSerializer(
                    com.android.sopkathon_android1.data.dto.hyemin.MissionBasketDto.serializer(),
                ),
            ),
            """{
                "status": 0,
                "code": "string",
                "message": "string",
                "data": [{
                    "missionBasketId": 12,
                    "missionId": 3,
                    "title": "셀프 사진 네 컷 찍어보기",
                    "imageUrl": "https://example.com/missions/3.jpg",
                    "createdAt": "2026-11-21T18:30:00"
                }]
            }""",
        )
        assertEquals(decoded.data!!.first(), response.data!!.first())

        val mission = FakeHyeminRepository(source, FakeHyeminReviewDataSource()).getMissions().first()
        assertEquals(12L, mission.missionBasketId)
        assertEquals(3L, mission.missionId)
        assertEquals(decoded.data!!.first().title, mission.title)
        assertEquals(decoded.data!!.first().imageUrl, mission.imageUrl)
        assertEquals(decoded.data!!.first().createdAt, mission.createdAt)
    }

    @Test fun `조회 오류 후 재시도하고 빈 결과를 정상 처리한다`() = runTest {
        val source = FakeHyeminDataSource().apply { scenario = FakeHyeminScenario.ERROR }
        val vm = viewModel(FakeHyeminRepository(source, FakeHyeminReviewDataSource()))
        advanceUntilIdle()
        assertTrue(vm.uiState.value.error)
        source.scenario = FakeHyeminScenario.EMPTY
        vm.loadMissions()
        advanceUntilIdle()
        assertFalse(vm.uiState.value.error)
        assertFalse(vm.uiState.value.loading)
        assertTrue(vm.uiState.value.missions.isEmpty())
    }

    @Test fun `모두의 도전은 인증글을 조회하고 좋아요를 반영한다`() = runTest {
        val source = FakeHyeminReviewDataSource()
        val vm = viewModel(FakeHyeminRepository(FakeHyeminDataSource(), source))
        advanceUntilIdle()
        vm.selectTab(ChallengeTab.OUR)
        runCurrent()
        assertTrue(vm.uiState.value.loading)
        advanceUntilIdle()
        val review = vm.uiState.value.reviews.first()
        assertEquals(1L, review.reviewId)
        assertEquals("민수", review.writerName)
        assertEquals("처음엔 어색했는데 재밌었어요.", review.content)
        assertEquals(4, review.likeCount)
        assertTrue(review.isLiked)
        vm.toggleReviewLike(review.reviewId)
        advanceUntilIdle()
        assertEquals(3, vm.uiState.value.reviews.first().likeCount)
        assertFalse(vm.uiState.value.reviews.first().isLiked)
        vm.loadMissions()
        advanceUntilIdle()
        assertEquals(3, vm.uiState.value.reviews.first().likeCount)
    }

    @Test fun `인증글 500 응답은 오류이고 재시도한 빈 응답은 정상이다`() = runTest {
        val source = FakeHyeminReviewDataSource().apply { scenario = FakeHyeminScenario.ERROR }
        val vm = viewModel(FakeHyeminRepository(FakeHyeminDataSource(), source))
        advanceUntilIdle()
        vm.selectTab(ChallengeTab.OUR)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.error)
        source.scenario = FakeHyeminScenario.EMPTY
        vm.loadMissions()
        advanceUntilIdle()
        assertFalse(vm.uiState.value.error)
        assertTrue(vm.uiState.value.reviews.isEmpty())
    }

    @Test fun `취소한 완료 요청은 미션 상태를 바꾸지 않는다`() = runTest {
        val source = FakeHyeminDataSource()
        val repository = FakeHyeminRepository(source, FakeHyeminReviewDataSource())
        val job = launch { source.completeMission("3", "content://photo", "후기") }
        runCurrent()
        job.cancel()
        advanceUntilIdle()
        assertFalse(repository.getMissions().first().completed)
        source.completeMission("3", "content://photo", "후기")
        assertTrue(repository.getMissions().first().completed)
    }
}
