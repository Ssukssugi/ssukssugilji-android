package com.sabo.feature.diary.write.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.write.DiaryWriteScreen


fun NavController.navigateToDiaryWrite(imageUri: Uri) {
    this.navigate(RouteModel.DiaryWrite(imageUri.toString()))
}

fun NavGraphBuilder.diaryWriteScreen(
    onClickBack: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit,
    navigateToHome: () -> Unit
) {
    composable<RouteModel.DiaryWrite> {
        DiaryWriteScreen(
            onClickBack = onClickBack,
            navigateToDiaryDetail = navigateToDiaryDetail,
            navigateToHome = navigateToHome
        )
    }
}