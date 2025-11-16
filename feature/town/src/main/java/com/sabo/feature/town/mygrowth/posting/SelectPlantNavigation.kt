package com.sabo.feature.town.mygrowth.posting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.SelectGrowthPlant
import com.sabo.core.navigator.toolkit.slideInFromBottom
import com.sabo.core.navigator.toolkit.slideOutToBottom
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut

fun NavController.navigateToGrowthSelectPlant() {
    navigate(SelectGrowthPlant)
}

fun NavGraphBuilder.selectPlantScreen(
    onClickBack: () -> Unit,
    onClickNext: () -> Unit
) {
    composable<SelectGrowthPlant>(
        enterTransition = slideInFromBottom(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToBottom()
    ) {
        SelectPlantScreen(
            onClickBack = onClickBack
        )
    }
}