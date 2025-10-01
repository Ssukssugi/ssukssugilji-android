package com.sabo.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.home.HomeScreen

fun NavController.navigateToHome(navOptions: NavOptions) {
    this.navigate(RouteModel.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    navigateToGallery: () -> Unit,
    navigateToPlantAdd: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit
) {
    composable<RouteModel.Home> {
        HomeScreen(
            navigateToGallery = navigateToGallery,
            navigateToPlantAdd = navigateToPlantAdd,
            navigateToProfile = navigateToProfile,
            navigateToDiaryDetail = navigateToDiaryDetail
        )
    }
}