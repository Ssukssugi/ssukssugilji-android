package com.sabo.core.network.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import com.sabo.core.network.model.request.SaveNewDiaryRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class MultipartUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createDiaryRequestBody(request: SaveNewDiaryRequest): okhttp3.RequestBody {
        val json = Json.encodeToString(request)
        return json.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun createImageMultipartBody(
        imageUri: Uri,
        partName: String = "plantImage_${System.currentTimeMillis()}"
    ): MultipartBody.Part? {
        return try {
            val compressedFile = getCompressedImage(context, imageUri)
            val requestBody = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, compressedFile.name, requestBody)
        } catch (e: Exception) {
            null
        }
    }

    private fun getCompressedImage(context: Context, filePath: Uri): File {
        val source = ImageDecoder.createSource(context.contentResolver, filePath)
        val bitmap = ImageDecoder.decodeBitmap(source)

        val fileName = getFileName(context, filePath) ?: "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.cacheDir, fileName)

        try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val byteArray = outputStream.toByteArray()
            val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            val fileOutputStream = FileOutputStream(file)
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            outputStream.flush()
            outputStream.close()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
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