package com.android.sopkathon_android1.feature.hyemin

import androidx.lifecycle.SavedStateHandle
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
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
        val repository = FakeHyeminRepository(FakeHyeminDataSource())
        val first = HyeminDetailViewModel(repository, SavedStateHandle(mapOf("missionId" to "movie")))
        advanceUntilIdle()
        assertEquals("movie", first.uiState.value.mission?.id)
        first.review.edit { append("다음에 이어서 작성") }
        first.setPhoto("content://photo")
        first.saveDraft()
        advanceUntilIdle()
        assertTrue(first.uiState.value.finished)

        val reopened = HyeminDetailViewModel(repository, SavedStateHandle(mapOf("missionId" to "movie")))
        advanceUntilIdle()
        assertEquals("다음에 이어서 작성", reopened.review.text.toString())
        assertEquals("content://photo", reopened.uiState.value.photo)
    }

    @Test fun `상세 완료 후 목록을 재조회하면 완료 상태가 반영된다`() = runTest {
        val repository = FakeHyeminRepository(FakeHyeminDataSource())
        val list = HyeminViewModel(repository)
        val detail = HyeminDetailViewModel(repository, SavedStateHandle(mapOf("missionId" to "fruit")))
        advanceUntilIdle()
        detail.setPhoto("content://photo")
        detail.review.edit { append("재미있었어요") }
        detail.completeMission()
        advanceUntilIdle()
        assertTrue(detail.uiState.value.finished)
        list.loadMissions()
        advanceUntilIdle()
        assertTrue(list.uiState.value.missions.first { it.id == "fruit" }.completed)
    }
}
