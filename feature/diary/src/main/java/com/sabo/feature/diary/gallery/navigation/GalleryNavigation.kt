package com.sabo.feature.diary.gallery.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.Gallery
import com.sabo.core.navigator.toolkit.slideInFromBottom
import com.sabo.core.navigator.toolkit.slideOutToBottom
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.diary.gallery.GalleryScreen

fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    navigate(Gallery, navOptions)
}

fun NavGraphBuilder.galleryScreen(
    onClickBack: () -> Unit,
    onClickNext: (Uri) -> Unit
) {
    composable<Gallery>(
        enterTransition = slideInFromBottom(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToBottom()
    ) {
        GalleryScreen(
            onClickBack = onClickBack,
            onClickNext = onClickNext
        )
    }
}