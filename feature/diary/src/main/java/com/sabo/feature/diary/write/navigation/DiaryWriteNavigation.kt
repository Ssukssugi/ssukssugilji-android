package com.sabo.feature.diary.write.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.DiaryEdit
import com.sabo.core.navigator.model.DiaryWrite
import com.sabo.core.navigator.toolkit.navTypeOf
import com.sabo.core.navigator.toolkit.slideInFromEnd
import com.sabo.core.navigator.toolkit.slideOutToEnd
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.diary.write.DiaryWriteScreen
import kotlin.reflect.typeOf


fun NavController.navigateToDiaryWrite(plantId: Long, imageUri: Uri) {
    this.navigate(DiaryWrite(plantId = plantId, imageUri = imageUri.toString()))
}

fun NavController.navigateToDiaryEdit(route: DiaryEdit) {
    this.navigate(route)
}

fun NavGraphBuilder.diaryWriteScreen(
    onClickBack: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit,
    navigateToHome: () -> Unit
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
            navigateToHome = navigateToHome
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
            navigateToHome = navigateToHome
        )
    }
}