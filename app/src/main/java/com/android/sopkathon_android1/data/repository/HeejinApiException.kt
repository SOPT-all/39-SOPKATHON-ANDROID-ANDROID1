package com.android.sopkathon_android1.data.repository

import java.io.IOException

class HeejinApiException(
    val status: Int,
    val code: String?,
) : IOException("Mission API failed: status=$status, code=$code")
