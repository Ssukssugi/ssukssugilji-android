package com.sabo.feature.diary.gallery

import android.net.Uri

data class GalleryUiState(
    val selectedImageUri: Uri? = null
)

sealed interface GalleryEvent