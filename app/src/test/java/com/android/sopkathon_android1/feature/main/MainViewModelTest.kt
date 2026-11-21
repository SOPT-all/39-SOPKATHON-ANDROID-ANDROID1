package com.android.sopkathon_android1.feature.main

import com.android.sopkathon_android1.data.datasource.fake.FakeMainDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeMainScenario
import com.android.sopkathon_android1.data.model.MainItem
import com.android.sopkathon_android1.data.repository.MainRepository
import com.android.sopkathon_android1.data.repository.fake.FakeMainRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `조회가 끝나기 전 로딩이고 완료되면 샘플 목록을 표시한다`() = runTest {
        val viewModel = createViewModel(FakeMainScenario.SUCCESS)
        runCurrent()
        assertEquals(MainUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MainUiState.Success)
        assertEquals(3, (state as MainUiState.Success).items.size)
    }

    @Test
    fun `정상 응답의 빈 목록은 오류가 아닌 빈 화면으로 표시한다`() = runTest {
        val viewModel = createViewModel(FakeMainScenario.EMPTY)
        advanceUntilIdle()
        assertEquals(MainUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `실패 후 재시도하면 다시 조회하고 성공 상태로 바뀐다`() = runTest {
        var calls = 0
        val items = listOf(MainItem("test", "테스트 항목"))
        val repository = object : MainRepository {
            override suspend fun getItems(): List<MainItem> {
                if (++calls == 1) throw IOException("조회 실패")
                return items
            }
        }
        val viewModel = MainViewModel(repository)
        advanceUntilIdle()
        assertEquals(MainUiState.Error, viewModel.uiState.value)

        viewModel.loadItems()
        advanceUntilIdle()

        assertEquals(2, calls)
        assertEquals(MainUiState.Success(items), viewModel.uiState.value)
    }

    @Test
    fun `새 조회는 진행 중인 조회를 취소하고 새 결과를 표시한다`() = runTest {
        var calls = 0
        var cancelled = false
        val repository = object : MainRepository {
            override suspend fun getItems(): List<MainItem> {
                if (++calls == 1) {
                    try {
                        awaitCancellation()
                    } finally {
                        cancelled = true
                    }
                }
                return emptyList()
            }
        }
        val viewModel = MainViewModel(repository)
        runCurrent()

        viewModel.loadItems()
        advanceUntilIdle()

        assertTrue(cancelled)
        assertEquals(2, calls)
        assertEquals(MainUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `코루틴 취소를 조회 오류로 표시하지 않는다`() = runTest {
        val repository = object : MainRepository {
            override suspend fun getItems(): List<MainItem> = throw CancellationException()
        }
        val viewModel = MainViewModel(repository)
        advanceUntilIdle()
        assertEquals(MainUiState.Loading, viewModel.uiState.value)
    }

    private fun createViewModel(scenario: FakeMainScenario): MainViewModel = MainViewModel(
        FakeMainRepository(FakeMainDataSource(scenario = scenario)),
    )
}
