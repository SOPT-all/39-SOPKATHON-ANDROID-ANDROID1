package com.android.sopkathon_android1.feature.hyemin

import androidx.lifecycle.SavedStateHandle
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminReviewDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminVerificationScenario
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HyeminDetailViewModelTest {
    @Before fun setup() { Dispatchers.setMain(StandardTestDispatcher()) }
    @After fun cleanup() { Dispatchers.resetMain() }

    @Test fun `목록에서 전달한 id로 상세를 조회하고 저장한 초안을 다시 연다`() = runTest {
        val repository = FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource())
        val first = HyeminDetailViewModel(repository, SavedStateHandle(mapOf("missionId" to "4")))
        advanceUntilIdle()
        assertEquals("4", first.uiState.value.mission?.missionId?.toString())
        assertEquals("먼작귀 극장판을 보고 새로운 즐거움을 발견해보세요.", first.uiState.value.description)
        assertEquals("1. 관람할 상영 시간 확인\n2. 영화 관람\n3. 아래에 사진과 후기를 등록", first.uiState.value.instructions)
        first.review.edit { append("다음에 이어서 작성") }
        first.setPhoto("content://photo")
        first.saveDraft()
        advanceUntilIdle()
        assertTrue(first.uiState.value.finished)

        val reopened = HyeminDetailViewModel(repository, SavedStateHandle(mapOf("missionId" to "4")))
        advanceUntilIdle()
        assertEquals("다음에 이어서 작성", reopened.review.text.toString())
        assertEquals("content://photo", reopened.uiState.value.photo)
    }

    @Test fun `상세 완료 후 목록을 재조회하면 완료 상태가 반영된다`() = runTest {
        val repository = FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource())
        val list = HyeminViewModel(repository)
        val detail = HyeminDetailViewModel(repository, SavedStateHandle(mapOf("missionId" to "3")))
        advanceUntilIdle()
        assertEquals("1. 셀프 사진관 또는 휴대폰 준비\n2. 좋아하는 표정으로 네 컷 촬영\n3. 아래에 사진과 후기를 등록", detail.uiState.value.instructions)
        assertEquals("좋아하는 표정으로 나만의 네 컷 사진을 남겨보세요.", detail.uiState.value.description)
        detail.setPhoto("content://photo")
        detail.review.edit { append("재미있었어요") }
        detail.completeMission()
        assertEquals(HyeminVerificationState.LOADING, detail.uiState.value.verificationState)
        assertTrue(detail.uiState.value.submitting)
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.SUCCESS, detail.uiState.value.verificationState)
        assertFalse(detail.uiState.value.finished)
        detail.completeMission() // 성공 Toast 표시 중 재전송하지 않는다.
        assertEquals(HyeminVerificationState.SUCCESS, detail.uiState.value.verificationState)
        detail.dismissVerificationToast()
        assertTrue(detail.uiState.value.finished)
        assertFalse(detail.uiState.value.canInteract)
        list.loadMissions()
        advanceUntilIdle()
        assertTrue(list.uiState.value.missions.first { it.missionId == 3L }.completed)
    }
    @Test fun `인증 실패는 입력을 유지하고 재시도하면 성공한다`() = runTest {
        val source = FakeHyeminDataSource()
        val vm = HyeminDetailViewModel(
            FakeHyeminRepository(source, FakeHyeminReviewDataSource()),
            SavedStateHandle(mapOf("missionId" to "3")),
        )
        advanceUntilIdle()
        vm.setPhoto("content://photo")
        vm.review.edit { append("재미있었어요") }
        source.verificationScenario = FakeHyeminVerificationScenario.FAILURE
        vm.completeMission()
        vm.completeMission() // 판독 중 중복 요청은 무시한다.
        assertEquals(HyeminVerificationState.LOADING, vm.uiState.value.verificationState)
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.FAILURE, vm.uiState.value.verificationState)
        assertEquals("content://photo", vm.uiState.value.photo)
        assertEquals("재미있었어요", vm.review.text.toString())
        assertFalse(vm.uiState.value.finished)
        assertFalse(source.isCompleted(3))
        vm.dismissVerificationToast()
        assertEquals(HyeminVerificationState.IDLE, vm.uiState.value.verificationState)
        source.verificationScenario = FakeHyeminVerificationScenario.SUCCESS
        vm.completeMission()
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.SUCCESS, vm.uiState.value.verificationState)
        assertTrue(source.isCompleted(3))
    }

    @Test fun `통신 실패도 실패 Toast 상태로 표시한다`() = runTest {
        val source = FakeHyeminDataSource()
        val vm = HyeminDetailViewModel(
            FakeHyeminRepository(source, FakeHyeminReviewDataSource()),
            SavedStateHandle(mapOf("missionId" to "3")),
        )
        advanceUntilIdle()
        source.verificationScenario = FakeHyeminVerificationScenario.ERROR
        vm.setPhoto("content://photo")
        vm.review.edit { append("후기") }
        vm.completeMission()
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.FAILURE, vm.uiState.value.verificationState)
        assertTrue(vm.uiState.value.canInteract)
        assertFalse(vm.uiState.value.finished)
    }

    @Test fun `필수 입력값이 없으면 인증을 요청하지 않는다`() = runTest {
        val vm = HyeminDetailViewModel(
            FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource()),
            SavedStateHandle(mapOf("missionId" to "3")),
        )
        advanceUntilIdle()
        vm.completeMission()
        assertEquals(HyeminVerificationState.IDLE, vm.uiState.value.verificationState)
        vm.setPhoto("content://photo")
        vm.review.edit { append("   ") }
        vm.completeMission()
        assertEquals(HyeminVerificationState.IDLE, vm.uiState.value.verificationState)
    }
}
