package com.android.sopkathon_android1.core.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response

class ResponseExtTest {
    @Test
    fun returnsCodeFromErrorResponse() {
        val response = errorResponse(
            """{"status":404,"code":"RESOURCE_NOT_FOUND","message":"리소스를 찾을 수 없습니다.","data":null}"""
        )

        assertEquals("RESOURCE_NOT_FOUND", response.errorCodeOrNull(Json))
    }

    @Test
    fun returnsNullForMalformedErrorResponse() {
        assertNull(errorResponse("not json").errorCodeOrNull(Json))
    }

    @Test
    fun returnsNullForSuccessfulResponse() {
        assertNull(Response.success(Unit).errorCodeOrNull(Json))
    }

    private fun errorResponse(body: String): Response<Unit> = Response.error(
        400,
        body.toResponseBody("application/json".toMediaType()),
    )
}
