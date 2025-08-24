package com.sabo.feature.diary.plantadd.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.plantadd.PlantAddRoute

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