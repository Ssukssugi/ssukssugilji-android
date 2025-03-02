package com.sabo.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import com.sabo.feature.login.loginNavGraph
import com.sabo.feature.signup.signUpNavGraph

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues = padding)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination
        ) {
            loginNavGraph(
                navigateToSignUp = navigator::navigateToSignUp
            )
            signUpNavGraph()
        }
    }
}