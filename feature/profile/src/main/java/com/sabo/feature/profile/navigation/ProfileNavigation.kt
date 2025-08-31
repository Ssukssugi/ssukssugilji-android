package com.sabo.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.profile.ProfileScreen

fun NavController.navigateToProfile() {
    this.navigate(RouteModel.Profile)
}

fun NavGraphBuilder.profileNavGraph(
    onClickBack: () -> Unit,
    onClickSetting: () -> Unit
) {
    composable<RouteModel.Profile> {
        ProfileScreen(
            onClickBack = onClickBack,
            onClickSetting = onClickSetting
        )
    }
}
