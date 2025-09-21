package com.sabo.core.network.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.sabo.core.network.model.request.SaveNewDiaryRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class MultipartUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createDiaryRequestBody(request: SaveNewDiaryRequest): okhttp3.RequestBody {
        val json = Json.encodeToString(request)
        return json.toRequestBody("application/json".toMediaType())
    }

    fun createImageMultipartBody(
        imageUri: Uri,
        partName: String = "plantImage_${System.currentTimeMillis()}"
    ): MultipartBody.Part? {
        return try {
            val file = uriToFile(context, imageUri)
            val requestBody = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData(partName, file.name, requestBody)
        } catch (e: Exception) {
            null
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val fileName = getFileName(context, uri) ?: "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.cacheDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return file
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }

        return fileName
    }
}