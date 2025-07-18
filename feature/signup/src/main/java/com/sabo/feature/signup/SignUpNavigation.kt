package com.sabo.feature.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToSignUp(
    navOptions: NavOptions = androidx.navigation.navOptions {
        popUpTo(this@navigateToSignUp.graph.startDestinationId) {
            inclusive = true
        }
    }
) {
    this.navigate(RouteModel.SignUp, navOptions)
}

fun NavGraphBuilder.signUpNavGraph(
    onCompletedSignUp: () -> Unit
) {
    composable<RouteModel.SignUp> {
        SignUpRoute(
            onCompletedSignUp = onCompletedSignUp
        )
    }
}