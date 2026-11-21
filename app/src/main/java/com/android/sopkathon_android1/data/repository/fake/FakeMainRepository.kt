package com.android.sopkathon_android1.data.repository.fake

import com.android.sopkathon_android1.data.datasource.fake.FakeMainDataSource
import com.android.sopkathon_android1.data.model.MainItem
import com.android.sopkathon_android1.data.repository.MainRepository
import javax.inject.Inject

class FakeMainRepository @Inject constructor(
    private val dataSource: FakeMainDataSource,
) : MainRepository {
    override suspend fun getItems(): List<MainItem> = dataSource.getItems()
}
