package com.sabo.feature.main

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sabo.feature.main.component.MainNavHost
import com.sabo.feature.main.component.MainNavigator
import com.sabo.feature.main.component.rememberMainNavigator

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator()
) {
    MainScreenContent(
        navigator = navigator
    )
}

@Composable
private fun MainScreenContent(
    navigator: MainNavigator,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .imePadding()
            .navigationBarsPadding()
            .statusBarsPadding(),
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                padding = padding
            )
        }
    )
}