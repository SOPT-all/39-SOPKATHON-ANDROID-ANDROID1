package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.data.api.ReviewApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteReviewRepositoryTest {
    @Test fun `모두의 도전 인증글을 실제 목록 응답으로 변환한다`() = runTest {
        val repository = repository { request ->
            assertEquals("GET", request.method)
            assertEquals("/v1/reviews", request.url.encodedPath)
            response(request, 200, """{"status":200,"code":"string","message":"ok","data":[{"reviewId":1,"imageUrl":"https://example.com/reviews/1.jpg","title":"셀프 사진","content":"재밌었어요","writerName":"민수","createdAt":"2026-11-22T14:30:00","likeCount":4,"isLiked":true}]}""")
        }
        val review = repository.getReviews().single()
        assertEquals(1L, review.reviewId)
        assertEquals("재밌었어요", review.content)
        assertEquals(4, review.likeCount)
        assertTrue(review.isLiked)
    }

    @Test fun `오류 응답의 코드를 보존한다`() = runTest {
        val repository = repository { request ->
            response(request, 500, """{"status":500,"code":"INTERNAL_SERVER_ERROR","message":"오류"}""")
        }
        val failure = try {
            repository.getReviews()
            error("실패해야 합니다")
        } catch (error: ReviewListException) {
            error
        }
        assertEquals(500, failure.httpStatus)
        assertEquals("INTERNAL_SERVER_ERROR", failure.code)
    }

    private fun repository(handler: (Request) -> Response): RemoteReviewRepository {
        val client = OkHttpClient.Builder().addInterceptor { chain -> handler(chain.request()) }.build()
        val api = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ReviewApi::class.java)
        return RemoteReviewRepository(api, Json)
    }

    private fun response(request: Request, status: Int, body: String): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(status)
        .message("test")
        .body(body.toResponseBody("application/json".toMediaType()))
        .build()
}
