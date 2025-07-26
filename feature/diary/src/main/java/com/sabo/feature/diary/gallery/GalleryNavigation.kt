package com.sabo.feature.diary.gallery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    navigate(RouteModel.Gallery, navOptions)
}

fun NavGraphBuilder.galleryScreen() {
    composable<RouteModel.Gallery> {
        GalleryScreen()
    }
}