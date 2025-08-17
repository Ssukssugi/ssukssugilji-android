package com.sabo.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

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
    navigateToGallery: () -> Unit
) {
    composable<RouteModel.Home> {
        HomeScreen(
            navigateToGallery = navigateToGallery
        )
    }
}