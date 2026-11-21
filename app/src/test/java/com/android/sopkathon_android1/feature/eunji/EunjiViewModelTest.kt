package com.android.sopkathon_android1.feature.eunji

import com.android.sopkathon_android1.data.datasource.fake.FakeEunjiDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeEunjiScenario
import com.android.sopkathon_android1.data.model.EunjiItem
import com.android.sopkathon_android1.data.repository.EunjiRepository
import com.android.sopkathon_android1.data.repository.fake.FakeEunjiRepository
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
class EunjiViewModelTest {
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
        val viewModel = createViewModel(FakeEunjiScenario.SUCCESS)
        runCurrent()
        assertEquals(EunjiUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is EunjiUiState.Success)
        assertEquals(3, (state as EunjiUiState.Success).items.size)
    }

    @Test
    fun `정상 응답의 빈 목록은 오류가 아닌 빈 화면으로 표시한다`() = runTest {
        val viewModel = createViewModel(FakeEunjiScenario.EMPTY)
        advanceUntilIdle()
        assertEquals(EunjiUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `실패 후 재시도하면 다시 조회하고 성공 상태로 바뀐다`() = runTest {
        var calls = 0
        val items = listOf(EunjiItem("test", "테스트 항목"))
        val repository = object : EunjiRepository {
            override suspend fun getItems(): List<EunjiItem> {
                if (++calls == 1) throw IOException("조회 실패")
                return items
            }
        }
        val viewModel = EunjiViewModel(repository)
        advanceUntilIdle()
        assertEquals(EunjiUiState.Error, viewModel.uiState.value)

        viewModel.loadItems()
        advanceUntilIdle()

        assertEquals(2, calls)
        assertEquals(EunjiUiState.Success(items), viewModel.uiState.value)
    }

    @Test
    fun `새 조회는 진행 중인 조회를 취소하고 새 결과를 표시한다`() = runTest {
        var calls = 0
        var cancelled = false
        val repository = object : EunjiRepository {
            override suspend fun getItems(): List<EunjiItem> {
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
        val viewModel = EunjiViewModel(repository)
        runCurrent()

        viewModel.loadItems()
        advanceUntilIdle()

        assertTrue(cancelled)
        assertEquals(2, calls)
        assertEquals(EunjiUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `코루틴 취소를 조회 오류로 표시하지 않는다`() = runTest {
        val repository = object : EunjiRepository {
            override suspend fun getItems(): List<EunjiItem> = throw CancellationException()
        }
        val viewModel = EunjiViewModel(repository)
        advanceUntilIdle()
        assertEquals(EunjiUiState.Loading, viewModel.uiState.value)
    }

    private fun createViewModel(scenario: FakeEunjiScenario): EunjiViewModel = EunjiViewModel(
        FakeEunjiRepository(FakeEunjiDataSource(scenario = scenario)),
    )
}
