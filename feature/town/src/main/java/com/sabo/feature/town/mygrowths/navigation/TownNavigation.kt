package com.sabo.feature.town.mygrowths.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.MyGrowth
import com.sabo.core.navigator.toolkit.slideInFromEnd
import com.sabo.core.navigator.toolkit.slideOutToEnd
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.town.mygrowths.MyGrowthScreen

fun NavController.navigateToMyGrowth() {
    navigate(MyGrowth)
}

fun NavGraphBuilder.myGrowthScreen(
    onClickBack: () -> Unit
) {
    composable<MyGrowth>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
        MyGrowthScreen(
            onClickBack = onClickBack,
            onClickPosting = {}
        )
    }
}