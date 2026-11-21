package com.android.sopkathon_android1.data.di

import com.android.sopkathon_android1.data.datasource.fake.FakeMainDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeMainScenario
import com.android.sopkathon_android1.data.repository.MainRepository
import com.android.sopkathon_android1.data.repository.fake.FakeMainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // 서버 연동 시 이 바인딩의 구현체를 실제 Repository로 교체한다.
    @Binds
    @Singleton
    abstract fun bindMainRepository(repository: FakeMainRepository): MainRepository
}

@Module
@InstallIn(SingletonComponent::class)
object FakeDataSourceModule {
    @Provides
    @Singleton
    fun provideMainDataSource(): FakeMainDataSource = FakeMainDataSource(
        scenario = FakeMainScenario.SUCCESS,
    )
}
