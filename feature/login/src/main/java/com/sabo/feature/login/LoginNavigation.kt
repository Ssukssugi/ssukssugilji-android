package com.sabo.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToLogin() {
    navigate(RouteModel.Login)
}

fun NavGraphBuilder.loginNavGraph(
    navigateToSignUp: () -> Unit
) {
    composable<RouteModel.Login> {
        LoginRoute(
            navigateToSignUp = navigateToSignUp
        )
    }
}