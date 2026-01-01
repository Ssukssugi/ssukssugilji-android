package com.sabo.feature.town

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.Town
import com.sabo.core.navigator.toolkit.tabFadeIn
import com.sabo.core.navigator.toolkit.tabFadeOut

fun NavController.navigateToTown(navOptions: NavOptions? = null) {
    navigate(route = Town, navOptions = navOptions)
}

fun NavGraphBuilder.townScreen() {
    composable<Town>(
        enterTransition = tabFadeIn(),
        exitTransition = tabFadeOut()
    ) {
        TownScreen()
    }
}
