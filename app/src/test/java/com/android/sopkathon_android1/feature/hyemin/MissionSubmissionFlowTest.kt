package com.android.sopkathon_android1.feature.hyemin

import androidx.lifecycle.SavedStateHandle
import com.android.sopkathon_android1.data.api.ImageApi
import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.datasource.SelectedImage
import com.android.sopkathon_android1.data.datasource.SelectedImageSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminReviewDataSource
import com.android.sopkathon_android1.data.datasource.remote.ImageRemoteDataSource
import com.android.sopkathon_android1.data.repository.MissionBasketRepository
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteImageRepository
import com.android.sopkathon_android1.data.repository.remote.RemoteMissionCompletionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class MissionSubmissionFlowTest {
    @get:Rule val temporaryFolder = TemporaryFolder()
    @Before fun setup() { Dispatchers.setMain(StandardTestDispatcher()) }
    @After fun cleanup() { Dispatchers.resetMain() }

    @Test fun `Swagger 업로드 성공 응답의 imageKey가 완료 등록 POST까지 전달된다`() = runTest {
        val requests = mutableListOf<String>()
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            requests += request.method + " " + request.url.encodedPath
            val responseBody = when (request.url.encodedPath) {
                "/v1/images" -> {
                    assertTrue(request.body!!.contentType().toString().startsWith("multipart/form-data"))
                    """{"status":0,"code":"string","message":"ok","data":{"imageKey":"missions/uploaded.jpg","imageUrl":"https://example.com/photo.jpg"}}"""
                }
                "/v1/mission-baskets/12/completion" -> {
                    val body = Buffer().also { request.body!!.writeTo(it) }.readUtf8()
                    assertEquals("""{"imageKey":"missions/uploaded.jpg","content":"후기"}""", body)
                    """{"status":0,"code":"string","message":"ok","data":{"verificationResult":"PASS"}}"""
                }
                else -> error("예상하지 못한 요청")
            }
            Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(responseBody.toResponseBody("application/json".toMediaType())).build()
        }.build()
        val retrofit = Retrofit.Builder().baseUrl("https://example.com/").client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()
        val localRepository = FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource())
        val vm = HyeminDetailViewModel(
            repository = localRepository,
            missionBasketRepository = object : MissionBasketRepository {
                override suspend fun getMissions() = localRepository.getMissions()
                override suspend fun getMissionDetail(missionId: Long) = localRepository.getMissionDetail(missionId)
            },
            imageRepository = RemoteImageRepository(ImageRemoteDataSource(retrofit.create(ImageApi::class.java)), Json),
            completionRepository = RemoteMissionCompletionRepository(retrofit.create(MissionApi::class.java), Json),
            selectedImageSource = object : SelectedImageSource {
                override suspend fun read(uri: String) = SelectedImage(
                    temporaryFolder.newFile("selected.jpg").apply { writeText("image-bytes") }, "image/jpeg",
                )
            },
            savedStateHandle = SavedStateHandle(mapOf("missionBasketId" to "12")),
        )
        advanceUntilIdle()
        assertEquals(12L, vm.uiState.value.mission?.missionBasketId)
        assertEquals(3L, vm.uiState.value.mission?.missionId)
        vm.setPhoto("content://selected")
        vm.review.edit { append("후기") }
        vm.completeMission()
        vm.uiState.first {
            it.verificationState == HyeminVerificationState.SUCCESS ||
                it.verificationState == HyeminVerificationState.FAILURE
        }

        assertEquals(listOf("POST /v1/images", "POST /v1/mission-baskets/12/completion"), requests)
        assertEquals(HyeminVerificationState.SUCCESS, vm.uiState.value.verificationState)
        assertTrue(vm.uiState.value.completionSaved)
    }
}
