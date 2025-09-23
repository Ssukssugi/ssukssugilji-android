package com.sabo.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.profile.PolicyScreen
import com.sabo.feature.profile.ProfileScreen
import com.sabo.feature.profile.SettingsScreen

fun NavController.navigateToProfile() {
    this.navigate(RouteModel.Profile)
}

fun NavController.navigateToSettings() {
    this.navigate(RouteModel.Settings)
}

fun NavGraphBuilder.profileNavGraph(
    onClickBack: () -> Unit,
    onClickSetting: () -> Unit,
    onClickFAQ: () -> Unit,
    onClickPolicy: () -> Unit
) {
    composable<RouteModel.Profile> {
        ProfileScreen(
            onClickBack = onClickBack,
            onClickSetting = onClickSetting,
            onClickFAQ = onClickFAQ,
            onClickPolicy = onClickPolicy
        )
    }
}

fun NavGraphBuilder.settingsNavGraph(
    onClickBack: () -> Unit,
    onClickLogout: () -> Unit,
    onClickDeleteAccount: () -> Unit
) {
    composable<RouteModel.Settings> {
        SettingsScreen(
            onClickBack = onClickBack,
            onClickLogout = onClickLogout,
            onClickDeleteAccount = onClickDeleteAccount
        )
    }
}

fun NavController.navigateToPolicy() {
    navigate(RouteModel.Policy)
}

fun NavGraphBuilder.policyNavGraph(
    onClickBack: () -> Unit,
    navigateToWebLink: (RouteModel.WebLink.Link) -> Unit
) {
    composable<RouteModel.Policy> {
        PolicyScreen(
            onClickBack = onClickBack,
            navigateToWebLink = navigateToWebLink
        )
    }
}
