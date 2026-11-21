package com.android.sopkathon_android1.data.datasource.remote

import com.android.sopkathon_android1.data.api.ImageApi
import java.io.File
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ImageRemoteDataSource @Inject constructor(
    private val imageApi: ImageApi,
) {
    suspend fun uploadImage(file: File, mimeType: String) = imageApi.uploadImage(
        MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = file.asRequestBody(mimeType.toMediaType()),
        ),
    )
}
