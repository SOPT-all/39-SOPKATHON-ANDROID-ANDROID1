package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.data.api.MissionApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteMissionBasketRepositoryTest {
    @Test fun `사용자 1의 미완료와 완료 목록을 각각 조회해 상태를 구분한다`() = runTest {
        val paths = mutableListOf<String>()
        val repository = repository { request ->
            val path = request.url.encodedPath
            paths += path
            val completed = path.endsWith("/completed")
            response(request, 200, """{"status":200,"code":"string","message":"ok","data":[{"missionBasketId":${if (completed) 21 else 12},"missionId":${if (completed) 4 else 3},"title":"미션","imageUrl":"https://example.com/image.jpg","createdAt":"2026-11-21T18:30:00"}]}""")
        }
        val missions = repository.getMissions()
        assertEquals(listOf("/v1/users/1/missions/incomplete", "/v1/users/1/missions/completed"), paths)
        assertEquals(2, missions.size)
        assertEquals(12L, missions[0].missionBasketId)
        assertFalse(missions[0].completed)
        assertEquals(21L, missions[1].missionBasketId)
        assertTrue(missions[1].completed)
    }

    @Test fun `미션 목록 오류 코드를 보존한다`() = runTest {
        val repository = repository { request ->
            response(request, 404, """{"status":404,"code":"USER_NOT_FOUND","message":"오류"}""")
        }
        val failure = try {
            repository.getMissions()
            error("실패해야 합니다")
        } catch (error: MissionBasketException) {
            error
        }
        assertEquals(404, failure.httpStatus)
        assertEquals("USER_NOT_FOUND", failure.code)
    }

    @Test fun `미션 상세 API의 설명과 수행 방법을 인증 화면 모델로 변환한다`() = runTest {
        val repository = repository { request ->
            assertEquals("GET", request.method)
            assertEquals("/v1/missions/4", request.url.encodedPath)
            response(request, 200, """{"status":200,"code":"string","message":"ok","data":{"description":"사진으로 기록해요","instructions":"사진을 찍고 후기를 작성해요"}}""")
        }
        val detail = repository.getMissionDetail(4L)
        assertEquals("사진으로 기록해요", detail.description)
        assertEquals("사진을 찍고 후기를 작성해요", detail.instructions)
    }

    private fun repository(handler: (Request) -> Response): RemoteMissionBasketRepository {
        val client = OkHttpClient.Builder().addInterceptor { chain -> handler(chain.request()) }.build()
        val api = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MissionApi::class.java)
        return RemoteMissionBasketRepository(api, Json)
    }

    private fun response(request: Request, status: Int, body: String): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(status)
        .message("test")
        .body(body.toResponseBody("application/json".toMediaType()))
        .build()
}
