package com.sabo.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sabo.core.designsystem.component.MainFAB
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.navigator.MainTabRoute
import com.sabo.core.navigator.model.Diary
import com.sabo.core.navigator.model.Profile
import com.sabo.core.navigator.model.Town
import com.sabo.feature.main.component.MainNavigationSuite
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
    var snackBarState by rememberSnackBarState()
    var isFabExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedPlantId by rememberSaveable { mutableStateOf<Long?>(null) }

    val navBackStackEntry by navigator.navController.currentBackStackEntryAsState()

    val currentTabRoute by remember(navBackStackEntry) {
        derivedStateOf {
            when (navBackStackEntry?.destination?.route) {
                Diary::class.qualifiedName -> MainTabRoute.Diary
                Town::class.qualifiedName -> MainTabRoute.Town
                Profile::class.qualifiedName -> MainTabRoute.Profile
                else -> null
            }
        }
    }

    val currentTab = currentTabRoute?.let { MainTab.fromRoute(it) } ?: MainTab.START_TAB

    val shouldShowBottomNav = currentTabRoute != null

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .imePadding()
                .navigationBarsPadding(),
            content = { padding ->
                Box {
                    if (shouldShowBottomNav) {
                        MainNavigationSuite(
                            currentTab = currentTab,
                            onTabSelected = { tabRoute ->
                                when (tabRoute) {
                                    MainTabRoute.Diary -> navigator.navigateToHome()
                                    MainTabRoute.Town -> navigator.navigateToTown()
                                    MainTabRoute.Profile -> navigator.navigateToProfile()
                                }
                            }
                        ) { innerPadding ->
                            MainNavHost(
                                navigator = navigator,
                                padding = innerPadding,
                                onShowSuccessSnackBar = { state -> snackBarState = state.copy(isVisible = true) },
                                onSelectedPlantIdChange = { plantId -> selectedPlantId = plantId }
                            )
                        }

                        AnimatedVisibility(
                            visible = isFabExpanded,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        isFabExpanded = false
                                    }
                            )
                        }

                        when (currentTab) {
                            MainTab.DIARY, MainTab.TOWN -> {
                                MainFAB(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(bottom = 80.dp),
                                    isExpanded = isFabExpanded,
                                    onExpandedChange = { isFabExpanded = it },
                                    onTownWriteClick = navigator::navigateToSelectGrowthPlant,
                                    onDiaryWriteClick = {
                                        selectedPlantId?.let { navigator.navigateToGallery(it) }
                                    }
                                )
                            }

                            MainTab.PROFILE -> {}
                        }
                    } else {
                        MainNavHost(
                            navigator = navigator,
                            padding = padding,
                            onShowSuccessSnackBar = { state -> snackBarState = state.copy(isVisible = true) },
                            onSelectedPlantIdChange = { plantId -> selectedPlantId = plantId }
                        )
                    }

                    if (snackBarState.isVisible) {
                        TopSnackBar(
                            modifier = modifier.padding(padding),
                            message = snackBarState.message,
                            iconRes = snackBarState.iconRes,
                            iconTint = snackBarState.iconTint,
                            onDismiss = { snackBarState = snackBarState.copy(isVisible = false) }
                        )
                    }
                }
            }
        )
    }
}