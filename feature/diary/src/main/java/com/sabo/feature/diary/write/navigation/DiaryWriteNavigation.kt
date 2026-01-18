package com.sabo.feature.diary.write.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.sabo.core.navigator.model.DiaryEdit
import com.sabo.core.navigator.model.DiaryWrite
import com.sabo.core.navigator.toolkit.navTypeOf
import com.sabo.core.navigator.toolkit.slideInFromEnd
import com.sabo.core.navigator.toolkit.slideOutToEnd
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.diary.write.DiaryWriteScreen
import kotlin.reflect.typeOf


fun NavController.navigateToDiaryWrite(plantId: Long?, imageUri: Uri? = null) {
    val navOptions = navOptions {
        popUpTo<DiaryWrite> {
            inclusive = true
        }
        launchSingleTop = true
    }
    this.navigate(
        route = DiaryWrite(plantId = plantId, imageUri = imageUri?.toString()),
        navOptions = navOptions
    )
}

fun NavController.navigateToDiaryEdit(route: DiaryEdit) {
    this.navigate(route)
}

fun NavGraphBuilder.diaryWriteScreen(
    onClickBack: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit,
    navigateToHome: () -> Unit,
    navigateToAddPlant: () -> Unit
) {
    composable<DiaryWrite>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
        DiaryWriteScreen(
            onClickBack = onClickBack,
            navigateToDiaryDetail = navigateToDiaryDetail,
            navigateToHome = navigateToHome,
            navigateToAddPlant = navigateToAddPlant
        )
    }

    composable<DiaryEdit>(
        typeMap = mapOf(
            typeOf<DiaryEdit>() to navTypeOf<DiaryEdit>()
        ),
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
        DiaryWriteScreen(
            onClickBack = onClickBack,
            navigateToDiaryDetail = navigateToDiaryDetail,
            navigateToHome = navigateToHome,
            navigateToAddPlant = navigateToAddPlant
        )
    }
}