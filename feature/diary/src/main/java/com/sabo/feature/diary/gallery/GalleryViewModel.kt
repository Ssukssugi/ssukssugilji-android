package com.sabo.feature.diary.gallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryImageManager: GalleryImageManager
) : ViewModel() {
    private val _state = MutableStateFlow(GalleryUiState())
    val state = _state.asStateFlow()

    private val _event = Channel<GalleryEvent>(Channel.RENDEZVOUS)
    val event = _event.receiveAsFlow()

    fun loadGalleryImages() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            try {
                val images = galleryImageManager.loadGalleyImages()
                _state.value = state.value.copy(images = images)
            } catch (e: Exception) {

            } finally {
                _state.value = state.value.copy(isLoading = false)
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        _state.value = state.value.copy(
            permissionGranted = granted,
            showPermissionDialog = granted.not()
        )

        if (granted) loadGalleryImages()
    }

    fun onImageSelected(uri: Uri) {
        _state.value = state.value.copy(selectedImageUri = if (state.value.selectedImageUri == uri) null else uri)
    }

    fun onImageCaptured(uri: Uri) {
        _state.value = state.value.copy(selectedImageUri = uri)
    }

    fun dismissPermissionDialog() {
        _state.value = state.value.copy(showPermissionDialog = false)
    }

    fun showPermissionDialog() {
        _state.value = state.value.copy(showPermissionDialog = true)
    }

    fun createImageUri(): Uri {
        return galleryImageManager.createImageUri()
    }
}