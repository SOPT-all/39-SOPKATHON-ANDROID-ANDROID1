package com.android.sopkathon_android1.data.di

import com.android.sopkathon_android1.data.datasource.ContentUriImageSource
import com.android.sopkathon_android1.data.datasource.SelectedImageSource
import com.android.sopkathon_android1.data.repository.EunjiRepository
import com.android.sopkathon_android1.data.repository.HeejinRepository
import com.android.sopkathon_android1.data.repository.HyeminRepository
import com.android.sopkathon_android1.data.repository.ImageRepository
import com.android.sopkathon_android1.data.repository.MissionCompletionRepository
import com.android.sopkathon_android1.data.repository.MissionBasketRepository
import com.android.sopkathon_android1.data.repository.ReviewRepository
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteEunjiRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteHeejinRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteImageRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteMissionCompletionRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteMissionBasketRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteReviewRepository
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
    abstract fun bindSelectedImageSource(source: ContentUriImageSource): SelectedImageSource

    @Binds
    @Singleton
    abstract fun bindHyeminRepository(repository: FakeHyeminRepository): HyeminRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(repository: RemoteImageRepository): ImageRepository

    @Binds
    @Singleton
    abstract fun bindMissionCompletionRepository(repository: RemoteMissionCompletionRepository): MissionCompletionRepository

    @Binds
    @Singleton
    abstract fun bindMissionBasketRepository(repository: RemoteMissionBasketRepository): MissionBasketRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(repository: RemoteReviewRepository): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindEunjiRepository(repository: RemoteEunjiRepository): EunjiRepository
}
