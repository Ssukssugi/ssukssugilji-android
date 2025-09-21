package com.sabo.feature.diary.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.detail.DiaryDetailScreen

fun NavController.navigateToDiaryDetail(plantId: Long, navOptions: NavOptions? = null) {
    navigate(
        route = RouteModel.DiaryDetail(plantId = plantId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.diaryDetailScreen(

) {
    composable<RouteModel.DiaryDetail> {
        DiaryDetailScreen(

        )
    }
}