package com.sabo.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.home.HomeScreen

fun NavController.navigateToHome(
    navOptions: NavOptions = androidx.navigation.navOptions {
        popUpTo(this@navigateToHome.graph.startDestinationId) {
            inclusive = true
        }
    }
) {
    this.navigate(RouteModel.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    navigateToGallery: () -> Unit,
    navigateToPlantAdd: () -> Unit
) {
    composable<RouteModel.Home> {
        HomeScreen(
            navigateToGallery = navigateToGallery,
            navigateToPlantAdd = navigateToPlantAdd
        )
    }
}