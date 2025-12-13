package com.sabo.feature.profile.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.ChangeProfile
import com.sabo.core.navigator.model.Policy
import com.sabo.core.navigator.model.Profile
import com.sabo.core.navigator.model.Settings
import com.sabo.core.navigator.model.UserDelete
import com.sabo.core.navigator.model.WebLink
import com.sabo.core.navigator.toolkit.slideInFromEnd
import com.sabo.core.navigator.toolkit.slideOutToEnd
import com.sabo.core.navigator.toolkit.tabFadeIn
import com.sabo.core.navigator.toolkit.tabFadeOut
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.profile.ProfileScreen
import com.sabo.feature.profile.ProfileViewModel
import com.sabo.feature.profile.changeprofile.ChangeProfileScreen
import com.sabo.feature.profile.delete.UserDeleteScreen
import com.sabo.feature.profile.policy.PolicyScreen
import com.sabo.feature.profile.settings.SettingsScreen

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
    onClickSetting: () -> Unit,
    onClickFAQ: () -> Unit,
    onClickPolicy: () -> Unit,
    onClickProfile: (String) -> Unit
) {
    composable<Profile>(
        enterTransition = tabFadeIn(),
        exitTransition = tabFadeOut()
    ) { backStackEntry ->
        val viewModel: ProfileViewModel = hiltViewModel()

        val isProfileUpdated by backStackEntry.savedStateHandle
            .getStateFlow(ChangeProfile.RESULT_PROFILE_UPDATED, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(isProfileUpdated) {
            if (isProfileUpdated) viewModel.reloadProfile()
        }

        ProfileScreen(
            onClickSetting = onClickSetting,
            onClickFAQ = onClickFAQ,
            onClickPolicy = onClickPolicy,
            onClickProfile = onClickProfile
        )
    }
}

fun NavGraphBuilder.settingsNavGraph(
    onClickBack: () -> Unit,
    navigateToLogin: () -> Unit,
    onClickDeleteAccount: () -> Unit
) {
    composable<Settings>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
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
    composable<UserDelete>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
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
    composable<Policy>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
        PolicyScreen(
            onClickBack = onClickBack,
            navigateToWebLink = navigateToWebLink
        )
    }
}

fun NavController.navigateToChangeProfile(nickname: String) {
    navigate(ChangeProfile(nickname))
}

fun NavController.popBackStackWithResultProfileUpdated() {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(ChangeProfile.RESULT_PROFILE_UPDATED, true)
    popBackStack()
}

fun NavGraphBuilder.changeProfileNavGraph(
    onClickBack: () -> Unit,
    onSucceedSave: () -> Unit
) {
    composable<ChangeProfile>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) {
        ChangeProfileScreen(
            onClickBack = onClickBack,
            onSucceedSave = onSucceedSave
        )
    }
}
