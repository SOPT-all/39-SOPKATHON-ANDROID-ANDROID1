package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.core.network.errorCodeOrNull
import com.android.sopkathon_android1.data.datasource.remote.ImageRemoteDataSource
import com.android.sopkathon_android1.data.datasource.MAX_IMAGE_SIZE_BYTES
import com.android.sopkathon_android1.data.model.ImageUploadValidationException
import com.android.sopkathon_android1.data.model.UploadedImage
import com.android.sopkathon_android1.data.repository.ImageRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class RemoteImageRepository @Inject constructor(
    private val dataSource: ImageRemoteDataSource,
    private val json: Json,
) : ImageRepository {
    override suspend fun uploadImage(file: File, mimeType: String): UploadedImage =
        withContext(Dispatchers.IO) {
            if (mimeType !in SUPPORTED_MIME_TYPES) {
                throw ImageUploadValidationException("IMAGE_TYPE_NOT_SUPPORTED")
            }
            if (!file.isFile || !file.canRead() || file.length() == 0L) {
                throw ImageUploadValidationException("IMAGE_EMPTY")
            }
            if (file.length() > MAX_IMAGE_SIZE_BYTES) {
                throw ImageUploadValidationException("IMAGE_SIZE_EXCEEDED")
            }

            val response = dataSource.uploadImage(file, mimeType)
            if (!response.isSuccessful) {
                throw ImageUploadException(response.code(), response.errorCodeOrNull(json))
            }
            val body = response.body()
            val image = body?.data
            if (body?.code != "IMAGE_UPLOAD_SUCCESS" || image == null || image.imageKey.isBlank() || image.imageUrl.isBlank()) {
                throw ImageUploadException(response.code(), body?.code)
            }
            UploadedImage(imageKey = image.imageKey, imageUrl = image.imageUrl)
        }

    private companion object {
        val SUPPORTED_MIME_TYPES = setOf("image/jpeg", "image/png", "image/webp")
    }
}

/** 화면에서는 서버 메시지가 아닌 [code]로 필요한 오류를 구분한다. */
class ImageUploadException(
    val httpStatus: Int,
    val code: String?,
) : IOException("이미지 업로드 실패 (HTTP $httpStatus, code=$code)")
