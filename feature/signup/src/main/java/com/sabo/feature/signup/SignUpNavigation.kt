package com.sabo.feature.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.SignUp

fun NavController.navigateToSignUp(
    navOptions: NavOptions = androidx.navigation.navOptions {
        popUpTo(this@navigateToSignUp.graph.startDestinationId) {
            inclusive = true
        }
    }
) {
    this.navigate(SignUp, navOptions)
}

fun NavGraphBuilder.signUpNavGraph(
    onCompletedSignUp: () -> Unit
) {
    composable<SignUp> {
        SignUpRoute(
            onCompletedSignUp = onCompletedSignUp
        )
    }
}