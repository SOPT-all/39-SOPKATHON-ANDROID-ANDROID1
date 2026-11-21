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
}
