package com.android.sopkathon_android1.feature.hyemin

import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminScenario
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
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

    @Test fun `조회 로딩 이후 미완료와 완료 미션을 제공한다`() = runTest {
        val vm = HyeminViewModel(FakeHyeminRepository(FakeHyeminDataSource()))
        runCurrent()
        assertTrue(vm.uiState.value.loading)
        advanceUntilIdle()
        assertEquals(2, vm.uiState.value.missions.count { !it.completed })
        assertEquals(1, vm.uiState.value.missions.count { it.completed })
    }

    @Test fun `조회 오류 후 재시도하고 빈 결과를 정상 처리한다`() = runTest {
        val source = FakeHyeminDataSource().apply { scenario = FakeHyeminScenario.ERROR }
        val vm = HyeminViewModel(FakeHyeminRepository(source))
        advanceUntilIdle()
        assertTrue(vm.uiState.value.error)
        source.scenario = FakeHyeminScenario.EMPTY
        vm.loadMissions()
        advanceUntilIdle()
        assertFalse(vm.uiState.value.error)
        assertFalse(vm.uiState.value.loading)
        assertTrue(vm.uiState.value.missions.isEmpty())
    }

    @Test fun `취소한 완료 요청은 미션 상태를 바꾸지 않는다`() = runTest {
        val source = FakeHyeminDataSource()
        val job = launch { source.completeMission("fruit", "content://photo", "후기") }
        runCurrent()
        job.cancel()
        advanceUntilIdle()
        assertFalse(source.getMissions().first().completed)
        source.completeMission("fruit", "content://photo", "후기")
        assertTrue(source.getMissions().first().completed)
    }
}
