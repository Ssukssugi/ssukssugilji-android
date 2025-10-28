package com.sabo.feature.main

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.sabo.feature.main.component.MainNavHost
import com.sabo.feature.main.component.MainNavigator
import com.sabo.feature.main.component.rememberMainNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.navigationEvent.collectLatest {
                navigator.navigateToLoginAndClearBackStack()
            }
        }
    }

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
            .navigationBarsPadding(),
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                padding = padding
            )
        }
    )
}