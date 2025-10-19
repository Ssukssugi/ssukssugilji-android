package com.sabo.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.Policy
import com.sabo.core.navigator.Profile
import com.sabo.core.navigator.Settings
import com.sabo.core.navigator.UserDelete
import com.sabo.core.navigator.WebLink
import com.sabo.core.navigator.model.RouteModel
import com.sabo.feature.profile.PolicyScreen
import com.sabo.feature.profile.ProfileScreen
import com.sabo.feature.profile.SettingsScreen
import com.sabo.feature.profile.UserDeleteScreen

fun NavController.navigateToProfile() {
    this.navigate(Profile)
}

fun NavController.navigateToSettings() {
    this.navigate(Settings)
}

fun NavController.navigateToUserDelete() {
    this.navigate(UserDelete)
}

fun NavGraphBuilder.profileNavGraph(
    onClickBack: () -> Unit,
    onClickSetting: () -> Unit,
    onClickFAQ: () -> Unit,
    onClickPolicy: () -> Unit
) {
    composable<Profile> {
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
    navigateToLogin: () -> Unit,
    onClickDeleteAccount: () -> Unit
) {
    composable<Settings> {
        SettingsScreen(
            onClickBack = onClickBack,
            navigateToLogin = navigateToLogin,
            navigateToUserDelete = onClickDeleteAccount
        )
    }
}

fun NavGraphBuilder.userDeleteNavGraph(
    onClickBack: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<UserDelete> {
        UserDeleteScreen(
            onClickBack = onClickBack,
            navigateToLogin = navigateToLogin
        )
    }
}

fun NavController.navigateToPolicy() {
    navigate(Policy)
}

fun NavGraphBuilder.policyNavGraph(
    onClickBack: () -> Unit,
    navigateToWebLink: (WebLink.Link) -> Unit
) {
    composable<Policy> {
        PolicyScreen(
            onClickBack = onClickBack,
            navigateToWebLink = navigateToWebLink
        )
    }
}
