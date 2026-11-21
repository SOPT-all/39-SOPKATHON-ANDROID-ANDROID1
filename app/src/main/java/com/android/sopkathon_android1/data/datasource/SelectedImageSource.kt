package com.android.sopkathon_android1.data.datasource

import java.io.File

data class SelectedImage(val file: File, val mimeType: String)

const val MAX_IMAGE_SIZE_BYTES = 10L * 1024 * 1024

interface SelectedImageSource {
    suspend fun read(uri: String): SelectedImage
}
