package com.sabo.feature.town.mygrowth.variation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.GrowthVariation
import com.sabo.core.navigator.toolkit.navTypeOf
import com.sabo.core.navigator.toolkit.slideInFromBottom
import com.sabo.core.navigator.toolkit.slideOutToBottom
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import kotlin.reflect.typeOf

fun NavController.navigateToGrowthVariation(
    route: GrowthVariation,
    navOptions: NavOptions? = null
) {
    navigate(
        route = route,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.growthVariationScreen(
    onClickBack: () -> Unit,
    onNavigateToHomeWithSuccess: () -> Unit
) {
    composable<GrowthVariation>(
        typeMap = mapOf(typeOf<GrowthVariation>() to navTypeOf<GrowthVariation>()),
        enterTransition = slideInFromBottom(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToBottom()
    ) {
        VariationScreen(
            onClickBack = onClickBack,
            onNavigateToHomeWithSuccess = onNavigateToHomeWithSuccess
        )
    }
}