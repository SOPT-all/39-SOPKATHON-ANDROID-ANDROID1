package com.android.sopkathon_android1.data.di

import com.android.sopkathon_android1.data.datasource.fake.FakeEunjiDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeEunjiScenario
import com.android.sopkathon_android1.data.repository.EunjiRepository
import com.android.sopkathon_android1.data.repository.fake.FakeEunjiRepository
import com.android.sopkathon_android1.data.repository.HeejinRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteHeejinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindHeejinRepository(repository: RemoteHeejinRepository): HeejinRepository

    @Binds
    @Singleton
    abstract fun bindHyeminRepository(repository: com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository): com.android.sopkathon_android1.data.repository.HyeminRepository

    // 서버 연동 시 이 바인딩의 구현체를 실제 Repository로 교체한다.
    @Binds
    @Singleton
    abstract fun bindEunjiRepository(repository: FakeEunjiRepository): EunjiRepository
}

@Module
@InstallIn(SingletonComponent::class)
object FakeDataSourceModule {
    @Provides
    @Singleton
    fun provideEunjiDataSource(): FakeEunjiDataSource = FakeEunjiDataSource(
        scenario = FakeEunjiScenario.SUCCESS,
    )
}
