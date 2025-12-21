package com.sabo.feature.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.Login

fun NavGraphBuilder.loginNavGraph(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToPolicy: () -> Unit,
    navigateToPrivacy: () -> Unit
) {
    composable<Login> {
        LoginRoute(
            navigateToSignUp = navigateToSignUp,
            navigateToHome = navigateToHome,
            navigateToPolicy = navigateToPolicy,
            navigateToPrivacy = navigateToPrivacy
        )
    }
}