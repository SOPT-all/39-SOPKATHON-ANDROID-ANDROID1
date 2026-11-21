package com.android.sopkathon_android1.data.di

import com.android.sopkathon_android1.data.repository.EunjiRepository
import com.android.sopkathon_android1.data.repository.HeejinRepository
import com.android.sopkathon_android1.data.repository.HyeminRepository
import com.android.sopkathon_android1.data.repository.ImageRepository
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteEunjiRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteHeejinRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteImageRepository
import dagger.Binds
import dagger.Module
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
    abstract fun bindHyeminRepository(repository: FakeHyeminRepository): HyeminRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(repository: RemoteImageRepository): ImageRepository

    // 서버 연동 시 이 바인딩의 구현체를 실제 Repository로 교체한다.
    @Binds
    @Singleton
    abstract fun bindEunjiRepository(repository: RemoteEunjiRepository): EunjiRepository
}
