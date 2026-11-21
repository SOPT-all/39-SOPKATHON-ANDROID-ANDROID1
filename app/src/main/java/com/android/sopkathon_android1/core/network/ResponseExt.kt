package com.android.sopkathon_android1.core.network

import com.android.sopkathon_android1.data.dto.base.BaseResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import retrofit2.Response

/** 오류 본문을 한 번 읽어 서버의 공통 응답 코드로 변환한다. */
fun Response<*>.errorCodeOrNull(json: Json): String? {
    if (isSuccessful) return null

    val body = runCatching { errorBody()?.string() }.getOrNull() ?: return null
    return runCatching {
        json.decodeFromString<BaseResponse<JsonElement>>(body).code
    }.getOrNull()
}
