package com.sabo.feature.diary.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.DiaryDetail
import com.sabo.core.navigator.model.DiaryEdit
import com.sabo.core.navigator.toolkit.slideInFromEnd
import com.sabo.core.navigator.toolkit.slideOutToEnd
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.diary.detail.DiaryDetailScreen

fun NavController.navigateToDiaryDetail(plantId: Long, diaryId: Long, navOptions: NavOptions? = null) {
    navigate(
        route = DiaryDetail(plantId = plantId, diaryId = diaryId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.diaryDetailScreen(
    onClickBack: () -> Unit,
    navigateToEditDiary: (DiaryEdit) -> Unit,
    popUpWithResult: () -> Unit
) {
    composable<DiaryDetail>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
        DiaryDetailScreen(
            onClickBack = onClickBack,
            navigateToEditDiary = navigateToEditDiary,
            popBackStackWithResult = popUpWithResult
        )
    }
}