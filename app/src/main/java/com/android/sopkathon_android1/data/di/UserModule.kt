package com.android.sopkathon_android1.data.di

import com.android.sopkathon_android1.data.repository.UserRepository
import com.android.sopkathon_android1.data.repository.UserRepositoryImpl
import com.android.sopkathon_android1.data.service.UserApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object UserApiModule {
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository
}
