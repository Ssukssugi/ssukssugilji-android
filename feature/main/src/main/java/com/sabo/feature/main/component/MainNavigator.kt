package com.sabo.feature.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.home.navigateToHome
import com.sabo.feature.login.navigateToLogin
import com.sabo.feature.signup.navigateToSignUp

class MainNavigator(
    val navController: NavHostController
) {
    val startDestination = RouteModel.Login

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigateToSignUp() {
        navController.navigateToSignUp()
    }

    fun navigateToHome() {
        navController.navigateToHome()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}