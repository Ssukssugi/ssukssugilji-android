package com.sabo.feature.diary.gallery

import android.net.Uri

data class GalleryUiState(
    val images: List<Uri> = emptyList(),
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val permissionGranted: Boolean = false
)

sealed interface GalleryEvent {

}