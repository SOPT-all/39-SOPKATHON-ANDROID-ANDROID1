package com.android.sopkathon_android1.data.repository.fake

import com.android.sopkathon_android1.data.datasource.fake.FakeEunjiDataSource
import com.android.sopkathon_android1.data.model.EunjiItem
import com.android.sopkathon_android1.data.repository.EunjiRepository
import javax.inject.Inject

class FakeEunjiRepository @Inject constructor(
    private val dataSource: FakeEunjiDataSource,
) : EunjiRepository {
    override suspend fun getItems(): List<EunjiItem> = dataSource.getItems()
}
