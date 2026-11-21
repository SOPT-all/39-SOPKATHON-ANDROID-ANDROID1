package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.UploadedImage
import java.io.File

interface ImageRepository {
    /** JPEG, PNG, WebP 파일을 업로드한다. 호출이 끝날 때까지 파일을 유지해야 한다. */
    suspend fun uploadImage(file: File, mimeType: String): UploadedImage
}
