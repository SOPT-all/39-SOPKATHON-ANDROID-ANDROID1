package com.android.sopkathon_android1.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.service.HeejinService
import com.android.sopkathon_android1.data.service.ImageService

private const val BASE_URL = "https://sopkathon-android1.duckdns.org/"

/**
 * 서버 API가 준비되면 Retrofit 인터페이스를 만든 뒤 아래 형태로 제공한다.
 * ```kotlin
 * @Provides
 * fun provideResourceApi(retrofit: Retrofit): ResourceApi =
 *     retrofit.create(ResourceApi::class.java)
 * ```
 * 인터페이스의 `@GET("resources/{id}")` 같은 경로는 [BASE_URL] 뒤에 붙는다.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHeejinService(retrofit: Retrofit): HeejinService =
        retrofit.create(HeejinService::class.java)

    @Provides
    @Singleton
    fun provideImageService(retrofit: Retrofit): ImageService =
        retrofit.create(ImageService::class.java)

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideMissionApi(retrofit: Retrofit): MissionApi =
        retrofit.create(MissionApi::class.java)
}
