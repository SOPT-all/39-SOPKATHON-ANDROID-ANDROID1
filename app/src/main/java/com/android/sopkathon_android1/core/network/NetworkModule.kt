package com.android.sopkathon_android1.core.network

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import com.android.sopkathon_android1.data.api.ImageApi
import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.api.ReviewApi
import com.android.sopkathon_android1.data.service.HeejinService
import com.android.sopkathon_android1.data.service.ImageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

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
    fun provideImageApi(retrofit: Retrofit): ImageApi = retrofit.create(ImageApi::class.java)

    @Provides
    @Singleton
    fun provideMissionApi(retrofit: Retrofit): MissionApi = retrofit.create(MissionApi::class.java)

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi = retrofit.create(ReviewApi::class.java)

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder().apply {
            if (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
                addInterceptor(HttpLoggingInterceptor { message ->
                    Log.d("MissionHttp", message)
                }.apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        }.build()

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

}
