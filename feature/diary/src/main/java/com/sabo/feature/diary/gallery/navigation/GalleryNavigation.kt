package com.sabo.feature.diary.gallery.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.gallery.GalleryScreen

fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    navigate(RouteModel.Gallery, navOptions)
}

fun NavGraphBuilder.galleryScreen(
    onClickBack: () -> Unit,
    onClickNext: (Uri) -> Unit
) {
    composable<RouteModel.Gallery> {
        GalleryScreen(
            onClickBack = onClickBack,
            onClickNext = onClickNext
        )
    }
}