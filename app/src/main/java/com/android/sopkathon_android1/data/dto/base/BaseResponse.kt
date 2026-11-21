package com.android.sopkathon_android1.data.dto.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 서버의 공통 응답 형식. 데이터가 없는 성공 응답과 오류 응답에서는 [data]가 null이다.
 * 화면에서는 변경될 수 있는 [message] 대신 [code]로 결과를 분기한다.
 *
 * API 작성 예시 (경로와 DTO 필드는 실제 서버 명세에 맞춘다):
 * ```kotlin
 * @Serializable
 * data class ResourceDto(val id: Long, val name: String)
 *
 * interface ResourceApi {
 *     @GET("resources/{id}")
 *     suspend fun getResource(@Path("id") id: Long): Response<BaseResponse<ResourceDto>>
 * }
 * ```
 *
 * Retrofit의 4xx/5xx 응답은 `body()` 대신 `errorBody()`에 들어오므로,
 * Repository에서 같은 형식으로 파싱할 수 있다:
 * ```kotlin
 * val response = resourceApi.getResource(id)
 * if (response.isSuccessful) {
 *     val resource = response.body()?.data
 * } else {
 *     val error = response.errorBody()?.string()?.let { raw ->
 *         runCatching { json.decodeFromString<BaseResponse<JsonElement>>(raw) }.getOrNull()
 *     }
 *     when (error?.code) {
 *         "RESOURCE_NOT_FOUND" -> Unit
 *         else -> Unit
 *     }
 * }
 * ```
 * `errorBody()`가 비어 있거나 JSON이 잘못된 경우도 처리해야 한다.
 */
@Serializable
data class BaseResponse<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
)
