package com.android.sopkathon_android1.data.datasource.fake

import com.android.sopkathon_android1.data.model.MainItem
import java.io.IOException
import kotlinx.coroutines.delay

enum class FakeMainScenario { SUCCESS, EMPTY, ERROR }

/** 개발 중 상태 확인용 데이터 소스. API 응답을 흉내 내는 DTO는 정의하지 않는다. */
class FakeMainDataSource(
    private val scenario: FakeMainScenario = FakeMainScenario.SUCCESS,
    private val delayMillis: Long = 500L,
) {
    suspend fun getItems(): List<MainItem> {
        delay(delayMillis)
        return when (scenario) {
            FakeMainScenario.SUCCESS -> listOf(
                MainItem(id = "sample-1", title = "샘플 항목 1"),
                MainItem(id = "sample-2", title = "샘플 항목 2"),
                MainItem(id = "sample-3", title = "샘플 항목 3"),
            )
            FakeMainScenario.EMPTY -> emptyList()
            FakeMainScenario.ERROR -> throw IOException("Mock 조회 실패")
        }
    }
}
