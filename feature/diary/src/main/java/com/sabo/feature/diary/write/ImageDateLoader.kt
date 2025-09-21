package com.sabo.feature.diary.write

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ImageDateLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getImageDateFromUri(uri: Uri): LocalDate {
        return try {
            val projection = arrayOf(MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATE_ADDED)

            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                    val dateAddedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)

                    val dateTaken = if (dateTakenIndex >= 0) {
                        cursor.getLong(dateTakenIndex)
                    } else 0L

                    val dateAdded = if (dateAddedIndex >= 0) {
                        cursor.getLong(dateAddedIndex) * 1000
                    } else 0L

                    val timestamp = if (dateTaken > 0) dateTaken else dateAdded

                    if (timestamp > 0) {
                        Instant.ofEpochMilli(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    } else {
                        LocalDate.now()
                    }
                } else {
                    LocalDate.now()
                }
            }
        } catch (e: Exception) {
            LocalDate.now()
        } ?: LocalDate.now()
    }
}