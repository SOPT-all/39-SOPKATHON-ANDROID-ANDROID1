package com.android.sopkathon_android1

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 앱 전체의 Hilt 시작점. 새 API를 연결할 때는 이 클래스를 수정하는 대신
 * `core/network/NetworkModule.kt` 같은 Hilt 모듈을 만든다.
 *
 * 서버 기본 주소가 정해진 뒤 적용할 모듈 예시 (주소는 예시이므로 실제 값으로 교체):
 * ```kotlin
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object NetworkModule {
 *     @Provides
 *     @Singleton
 *     fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
 *
 *     @Provides
 *     @Singleton
 *     fun provideRetrofit(client: OkHttpClient): Retrofit {
 *         val json = Json { ignoreUnknownKeys = true }
 *         return Retrofit.Builder()
 *             .baseUrl("https://YOUR_SERVER_URL/")
 *             .client(client)
 *             .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
 *             .build()
 *     }
 *
 *     @Provides
 *     fun provideResourceApi(retrofit: Retrofit): ResourceApi =
 *         retrofit.create(ResourceApi::class.java)
 * }
 * ```
 * 필요한 import는 IDE에서 추가하고, 실제 서버 주소는 빌드 설정으로 관리한다.
 */
@HiltAndroidApp
class SopkathonApplication : Application()
