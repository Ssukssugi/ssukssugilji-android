package com.sabo.feature.diary.gallery.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sabo.core.navigator.model.Gallery
import com.sabo.core.navigator.toolkit.slideInFromBottom
import com.sabo.core.navigator.toolkit.slideOutToBottom
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.diary.gallery.GalleryScreen

fun NavController.navigateToGallery(plantId: Long?, navOptions: NavOptions? = null) {
    navigate(Gallery(plantId = plantId), navOptions)
}

fun NavGraphBuilder.galleryScreen(
    onClickBack: () -> Unit,
    onClickNext: (Long?, Uri) -> Unit
) {
    composable<Gallery>(
        enterTransition = slideInFromBottom(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToBottom()
    ) { entry ->
        val plantId = entry.savedStateHandle.toRoute<Gallery>().plantId

        GalleryScreen(
            plantId = plantId,
            onClickBack = onClickBack,
            onClickNext = onClickNext
        )
    }
}