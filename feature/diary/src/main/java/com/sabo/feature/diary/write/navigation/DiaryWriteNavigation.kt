package com.sabo.feature.diary.write.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.DiaryWrite
import com.sabo.feature.diary.write.DiaryWriteScreen


fun NavController.navigateToDiaryWrite(imageUri: Uri) {
    this.navigate(DiaryWrite(imageUri.toString()))
}

fun NavGraphBuilder.diaryWriteScreen(
    onClickBack: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit,
    navigateToHome: () -> Unit
) {
    composable<DiaryWrite> {
        DiaryWriteScreen(
            onClickBack = onClickBack,
            navigateToDiaryDetail = navigateToDiaryDetail,
            navigateToHome = navigateToHome
        )
    }
}