package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.data.api.MissionApi
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteMissionCompletionRepositoryTest {
    @Test fun `imageKey와 후기를 basket ID로 보내고 PASS를 변환한다`() = runTest {
        val repository = repository { request ->
            assertEquals("POST", request.method)
            assertEquals("/v1/mission-baskets/12/completion", request.url.encodedPath)
            assertTrue(request.body!!.contentType().toString().startsWith("application/json"))
            val body = Buffer().also { request.body!!.writeTo(it) }.readUtf8()
            assertEquals("""{"imageKey":"missions/photo.jpg","content":"재미있었어요"}""", body)
            response(request, 200, """{"status":200,"code":"string","message":"ok","data":{"verificationResult":"PASS"}}""")
        }
        assertEquals(MissionVerificationResult.SUCCESS, repository.completeMission(12L, "missions/photo.jpg", "재미있었어요"))
    }

    @Test fun `FAIL 응답도 저장된 완료 결과로 반환한다`() = runTest {
        val repository = repository { request ->
            response(request, 200, """{"status":200,"code":"string","message":"ok","data":{"verificationResult":"FAIL"}}""")
        }
        assertEquals(MissionVerificationResult.FAILURE, repository.completeMission(12L, "missions/photo.jpg", "후기"))
    }

    @Test fun `HTTP 오류의 서버 코드를 보존한다`() = runTest {
        val repository = repository { request ->
            response(request, 503, """{"status":503,"code":"MISSION_AI_UNAVAILABLE","message":"오류","data":null}""")
        }
        val error = try {
            repository.completeMission(12L, "missions/photo.jpg", "후기")
            error("실패해야 합니다")
        } catch (error: MissionCompletionException) {
            error
        }
        assertEquals(503, error.httpStatus)
        assertEquals("MISSION_AI_UNAVAILABLE", error.code)
    }

    @Test fun `결과가 없거나 알 수 없는 값이면 성공으로 처리하지 않는다`() = runTest {
        for (data in listOf("null", """{"verificationResult":"UNKNOWN"}""")) {
            val repository = repository { request ->
                response(request, 200, """{"status":200,"code":"OK","message":"ok","data":$data}""")
            }
            try {
                repository.completeMission(12L, "missions/photo.jpg", "후기")
                error("실패해야 합니다")
            } catch (_: MissionCompletionException) {
                // 결과가 없으면 저장 완료 여부를 알 수 없다.
            }
        }
    }

    private fun repository(handler: (Request) -> Response): RemoteMissionCompletionRepository {
        val client = OkHttpClient.Builder().addInterceptor { chain -> handler(chain.request()) }.build()
        val api = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MissionApi::class.java)
        return RemoteMissionCompletionRepository(api, Json)
    }

    private fun response(request: Request, status: Int, body: String): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(status)
        .message("test")
        .body(body.toResponseBody("application/json".toMediaType()))
        .build()
}
