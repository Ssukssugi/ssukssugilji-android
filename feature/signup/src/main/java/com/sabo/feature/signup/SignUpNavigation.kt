package com.sabo.feature.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToSignUp() {
    navigate(RouteModel.SignUp)
}

fun NavGraphBuilder.signUpNavGraph() {
    composable<RouteModel.SignUp> {
        SignUpRoute()
    }
}