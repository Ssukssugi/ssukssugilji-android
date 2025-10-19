package com.sabo.feature.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.Login

fun NavGraphBuilder.loginNavGraph(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit
) {
    composable<Login> {
        LoginRoute(
            navigateToSignUp = navigateToSignUp,
            navigateToHome = navigateToHome
        )
    }
}