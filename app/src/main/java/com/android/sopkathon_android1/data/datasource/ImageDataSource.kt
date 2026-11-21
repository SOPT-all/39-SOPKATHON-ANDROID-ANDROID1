package com.android.sopkathon_android1.data.datasource

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ImageDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun createImagePart(photoUri: String): MultipartBody.Part = withContext(Dispatchers.IO) {
        val uri = Uri.parse(photoUri)
        val resolver = context.contentResolver
        val mediaType = resolver.getType(uri)?.toMediaTypeOrNull()
        val fileName = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        } ?: "image"
        val body = object : RequestBody() {
            override fun contentType() = mediaType

            override fun writeTo(sink: BufferedSink) {
                val input = resolver.openInputStream(uri) ?: throw IOException("Unable to open selected image")
                input.source().use { sink.writeAll(it) }
            }
        }
        MultipartBody.Part.createFormData("file", fileName, body)
    }
}
