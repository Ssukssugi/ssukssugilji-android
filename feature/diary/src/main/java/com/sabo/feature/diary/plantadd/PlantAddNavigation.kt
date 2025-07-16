package com.sabo.feature.diary.plantadd

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToPlantAdd() {
    this.navigate(RouteModel.PlantAdd)
}

fun NavGraphBuilder.plantAddNavGraph(
    onClickCategory: (String) -> Unit,
    onClickHome: () -> Unit,
    onClickDiary: () -> Unit,
) {
    composable<RouteModel.PlantAdd>{
        PlantAddRoute(
            onClickCategory = onClickCategory,
            onClickHome = onClickHome,
            onClickDiary = onClickDiary
        )
    }
}