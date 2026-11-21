package com.android.sopkathon_android1.data.datasource

import android.content.Context
import android.net.Uri
import com.android.sopkathon_android1.data.model.ImageUploadValidationException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class ContentUriImageSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : SelectedImageSource {
    override suspend fun read(uri: String): SelectedImage = withContext(Dispatchers.IO) {
        val selectedUri = Uri.parse(uri)
        val mimeType = context.contentResolver.getType(selectedUri)
            ?: throw ImageUploadValidationException("IMAGE_TYPE_NOT_SUPPORTED")
        if (mimeType !in SUPPORTED_MIME_TYPES) {
            throw ImageUploadValidationException("IMAGE_TYPE_NOT_SUPPORTED")
        }

        val suffix = when (mimeType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            else -> throw ImageUploadValidationException("IMAGE_TYPE_NOT_SUPPORTED")
        }
        val file = File.createTempFile("mission-image-", suffix, context.cacheDir)
        try {
            val input = context.contentResolver.openInputStream(selectedUri)
                ?: throw IOException("선택한 이미지를 읽을 수 없습니다.")
            input.use { source ->
                file.outputStream().use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var size = 0L
                    while (true) {
                        currentCoroutineContext().ensureActive()
                        val count = source.read(buffer)
                        if (count < 0) break
                        size += count
                        if (size > MAX_IMAGE_SIZE_BYTES) {
                            throw ImageUploadValidationException("IMAGE_SIZE_EXCEEDED")
                        }
                        output.write(buffer, 0, count)
                    }
                }
            }
            currentCoroutineContext().ensureActive()
            if (file.length() == 0L) throw ImageUploadValidationException("IMAGE_EMPTY")
            SelectedImage(file, mimeType)
        } catch (error: Exception) {
            file.delete()
            throw error
        }
    }

    private companion object {
        val SUPPORTED_MIME_TYPES = setOf("image/jpeg", "image/png")
    }
}
