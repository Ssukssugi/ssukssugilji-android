package com.sabo.feature.diary.plantadd

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToPlantAdd() {
    this.navigate(RouteModel.PlantAdd)
}

fun NavGraphBuilder.loginPlantAddGraph() {
    composable<RouteModel.PlantAdd>{
        PlantAddRoute()
    }
}