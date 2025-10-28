package com.sabo.feature.diary.gallery.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.Gallery
import com.sabo.feature.diary.gallery.GalleryScreen

fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    navigate(Gallery, navOptions)
}

fun NavGraphBuilder.galleryScreen(
    onClickBack: () -> Unit,
    onClickNext: (Uri) -> Unit
) {
    composable<Gallery> {
        GalleryScreen(
            onClickBack = onClickBack,
            onClickNext = onClickNext
        )
    }
}