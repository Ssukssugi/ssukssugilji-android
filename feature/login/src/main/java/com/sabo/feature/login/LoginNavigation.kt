package com.sabo.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToLogin(
    navOptions: NavOptions = androidx.navigation.navOptions {
        popUpTo<RouteModel.Login> {
            inclusive = true
        }
    }
) {
    this.navigate(RouteModel.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit
) {
    composable<RouteModel.Login> {
        LoginRoute(
            navigateToSignUp = navigateToSignUp,
            navigateToHome = navigateToHome
        )
    }
}