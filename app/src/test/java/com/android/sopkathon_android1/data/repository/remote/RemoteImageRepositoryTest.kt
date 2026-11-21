package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.data.api.ImageApi
import com.android.sopkathon_android1.data.datasource.remote.ImageRemoteDataSource
import com.android.sopkathon_android1.data.model.ImageUploadValidationException
import java.io.File
import kotlinx.coroutines.CancellationException
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
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteImageRepositoryTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun `지원 이미지의 file 파트와 원본 바이트를 보내고 응답을 변환한다`() = runTest {
        for (mimeType in listOf("image/jpeg", "image/png")) {
            val repository = repository { request ->
                assertEquals("POST", request.method)
                assertEquals("/v1/images", request.url.encodedPath)
                assertTrue(request.body!!.contentType().toString().startsWith("multipart/form-data;"))
                val body = Buffer().also { request.body!!.writeTo(it) }.readUtf8()
                assertTrue(body.contains("name=\"file\"; filename=\"upload.jpg\""))
                assertTrue(body.contains("Content-Type: $mimeType"))
                assertTrue(body.contains("image-bytes"))
                response(request, 200, """{"status":200,"code":"IMAGE_UPLOAD_SUCCESS","message":"ok","data":{"imageKey":"missions/test.jpg","imageUrl":"https://example.com/test.jpg"}}""")
            }

            val result = repository.uploadImage(imageFile(), mimeType)

            assertEquals("missions/test.jpg", result.imageKey)
            assertEquals("https://example.com/test.jpg", result.imageUrl)
        }
    }

    @Test
    fun `HTTP 오류에서 서버 코드를 보존한다`() = runTest {
        val repository = repository { request ->
            response(request, 400, """{"status":400,"code":"IMAGE_TYPE_NOT_SUPPORTED","message":"오류","data":null}""")
        }
        val error = expectFailure<ImageUploadException> { repository.uploadImage(imageFile(), "image/jpeg") }
        assertEquals(400, error.httpStatus)
        assertEquals("IMAGE_TYPE_NOT_SUPPORTED", error.code)
    }

    @Test
    fun `오류 본문이 JSON이 아니어도 HTTP 상태를 보존한다`() = runTest {
        val repository = repository { request -> response(request, 502, "Bad Gateway") }
        val error = expectFailure<ImageUploadException> { repository.uploadImage(imageFile(), "image/jpeg") }
        assertEquals(502, error.httpStatus)
        assertEquals(null, error.code)
    }

    @Test
    fun `성공 응답에 업로드 결과가 없으면 실패한다`() = runTest {
        val repository = repository { request ->
            response(request, 200, """{"status":200,"code":"IMAGE_UPLOAD_SUCCESS","message":"ok","data":null}""")
        }
        expectFailure<ImageUploadException> { repository.uploadImage(imageFile(), "image/jpeg") }
    }

    @Test
    fun `지원하지 않는 형식과 빈 파일은 요청하지 않는다`() = runTest {
        val repository = repository { error("네트워크 요청이 발생하면 안 됩니다.") }
        for (mimeType in listOf("image/gif", "image/webp", "image/heic")) {
            assertEquals("IMAGE_TYPE_NOT_SUPPORTED", expectFailure<ImageUploadValidationException> { repository.uploadImage(imageFile(), mimeType) }.code)
        }
        assertEquals("IMAGE_EMPTY", expectFailure<ImageUploadValidationException> {
            repository.uploadImage(temporaryFolder.newFile(), "image/png")
        }.code)
    }

    @Test
    fun `10MB 초과 이미지는 서버에 보내지 않는다`() = runTest {
        val repository = repository { error("네트워크 요청이 발생하면 안 됩니다.") }
        val oversized = temporaryFolder.newFile().apply { writeBytes(ByteArray(10 * 1024 * 1024 + 1)) }
        val error = expectFailure<ImageUploadValidationException> { repository.uploadImage(oversized, "image/jpeg") }
        assertEquals("IMAGE_SIZE_EXCEEDED", error.code)
    }

    @Test
    fun `Swagger에 성공 코드가 지정되지 않아도 200과 유효한 업로드 결과면 성공한다`() = runTest {
        val repository = repository { request ->
            response(request, 200, """{"status":0,"code":"string","message":"ok","data":{"imageKey":"missions/test.jpg","imageUrl":"https://example.com/test.jpg"}}""")
        }
        val image = repository.uploadImage(imageFile(), "image/jpeg")
        assertEquals("missions/test.jpg", image.imageKey)
    }

    @Test
    fun `코루틴 취소를 업로드 오류로 바꾸지 않는다`() = runTest {
        val api = object : ImageApi {
            override suspend fun uploadImage(file: okhttp3.MultipartBody.Part): retrofit2.Response<com.android.sopkathon_android1.data.dto.base.BaseResponse<com.android.sopkathon_android1.data.dto.image.ImageUploadResponse>> {
                throw CancellationException("취소")
            }
        }
        val repository = RemoteImageRepository(ImageRemoteDataSource(api), Json)
        expectFailure<CancellationException> { repository.uploadImage(imageFile(), "image/jpeg") }
    }

    private fun imageFile(): File = File(temporaryFolder.root, "upload.jpg").apply { writeText("image-bytes") }

    private fun repository(handler: (Request) -> Response): RemoteImageRepository {
        val client = OkHttpClient.Builder().addInterceptor { chain -> handler(chain.request()) }.build()
        val api = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ImageApi::class.java)
        return RemoteImageRepository(ImageRemoteDataSource(api), Json)
    }

    private fun response(request: Request, status: Int, body: String): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(status)
        .message("test")
        .body(body.toResponseBody("application/json".toMediaType()))
        .build()

    private suspend inline fun <reified T : Throwable> expectFailure(block: () -> Unit): T {
        try {
            block()
        } catch (error: Throwable) {
            if (error is T) return error
            throw error
        }
        error("${T::class.java.simpleName} 예외가 필요합니다.")
    }
}
