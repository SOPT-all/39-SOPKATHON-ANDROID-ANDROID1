package com.android.sopkathon_android1.data.dto.base

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BaseResponseTest {
    @Serializable
    private data class ResourceDto(val id: Int, val name: String)

    @Test
    fun parsesSuccessWithData() {
        val response = Json.decodeFromString<BaseResponse<ResourceDto>>(
            """{"status":200,"code":"RESOURCE_READ_SUCCESS","message":"리소스 조회에 성공했습니다.","data":{"id":1,"name":"리소스 이름"}}"""
        )

        assertEquals(200, response.status)
        assertEquals("RESOURCE_READ_SUCCESS", response.code)
        assertEquals("리소스 조회에 성공했습니다.", response.message)
        assertEquals(ResourceDto(1, "리소스 이름"), response.data)
    }

    @Test
    fun parsesErrorWithNullData() {
        val response = Json.decodeFromString<BaseResponse<ResourceDto>>(
            """{"status":404,"code":"RESOURCE_NOT_FOUND","message":"리소스를 찾을 수 없습니다.","data":null}"""
        )

        assertEquals(404, response.status)
        assertEquals("RESOURCE_NOT_FOUND", response.code)
        assertNull(response.data)
    }
    @Test
    fun parsesReviewErrorWithoutData() {
        val response = Json.decodeFromString<BaseResponse<List<com.android.sopkathon_android1.data.dto.hyemin.MissionReviewDto>>>(
            """{"status":500,"code":"INTERNAL_SERVER_ERROR","message":"오류가 발생했습니다. 다시 시도해 주세요."}"""
        )
        assertEquals("INTERNAL_SERVER_ERROR", response.code)
        assertNull(response.data)
    }

    @Test
    fun parsesReviewSuccess() {
        val response = Json.decodeFromString<BaseResponse<List<com.android.sopkathon_android1.data.dto.hyemin.MissionReviewDto>>>(
            """{"status":0,"code":"string","message":"string","data":[{"reviewId":1,"imageUrl":"https://example.com/reviews/1.jpg","title":"셀프 사진 네 컷 찍어보기","content":"처음엔 어색했는데 재밌었어요.","writerName":"민수","createdAt":"2026-11-22T14:30:00","likeCount":4,"isLiked":true}]}"""
        )
        val review = response.data!!.single()
        assertEquals(1L, review.reviewId)
        assertEquals(4, review.likeCount)
        assertEquals(true, review.isLiked)
    }
}
