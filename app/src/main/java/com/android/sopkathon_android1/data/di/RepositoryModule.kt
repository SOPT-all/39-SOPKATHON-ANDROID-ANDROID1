package com.android.sopkathon_android1.data.di

import com.android.sopkathon_android1.data.repository.EunjiRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteEunjiRepository
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
    abstract fun bindEunjiRepository(repository: RemoteEunjiRepository): EunjiRepository
}
