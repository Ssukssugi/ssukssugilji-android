package com.sabo.feature.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.login.navigateToLogin

class MainNavigator(
    val navController: NavHostController
) {
    val startDestination = RouteModel.Login

    fun navigateToLogin() {
        navController.navigateToLogin()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}