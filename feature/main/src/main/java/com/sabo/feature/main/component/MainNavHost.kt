package com.sabo.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.detail.navigation.diaryDetailScreen
import com.sabo.feature.diary.gallery.navigation.galleryScreen
import com.sabo.feature.diary.plantadd.categorySearch.navigation.categorySearchScreen
import com.sabo.feature.diary.plantadd.categorySearch.navigation.popBackStackWithResult
import com.sabo.feature.diary.plantadd.navigation.plantAddNavGraph
import com.sabo.feature.diary.write.navigation.diaryWriteScreen
import com.sabo.feature.home.navigation.homeNavGraph
import com.sabo.feature.login.loginNavGraph
import com.sabo.feature.profile.navigation.policyNavGraph
import com.sabo.feature.profile.navigation.profileNavGraph
import com.sabo.feature.profile.navigation.settingsNavGraph
import com.sabo.feature.profile.navigation.userDeleteNavGraph
import com.sabo.feature.signup.signUpNavGraph
import com.sabo.feature.web.navigation.webLinkScreen

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues = padding)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = RouteModel.Login
        ) {
            loginNavGraph(
                navigateToSignUp = navigator::navigateToSignUp,
                navigateToHome = navigator::navigateToHome
            )
            signUpNavGraph(
                onCompletedSignUp = navigator::navigateToHome
            )
            homeNavGraph(
                navigateToGallery = navigator::navigateToGallery,
                navigateToPlantAdd = navigator::navigateToPlantAdd,
                navigateToProfile = navigator::navigateToProfile,
                navigateToDiaryDetail = navigator::navigateToDiaryDetail
            )
            plantAddNavGraph(
                onClickCategory = navigator::navigateToCategorySearch,
                onClickHome = navigator::navigateToHome,
                onClickDiary = { /* TODO: 일지 작성 하러 가기 추가*/ },
                onClickBack = navigator::popBackStack
            )
            categorySearchScreen(
                onClickBack = navigator::popBackStack,
                onClickCategory = navigator.navController::popBackStackWithResult
            )
            galleryScreen(
                onClickBack = navigator::popBackStack,
                onClickNext = navigator::navigateToDiaryWrite
            )
            diaryWriteScreen(
                onClickBack = navigator::popBackStack,
                navigateToHome = navigator::navigateToHome,
                navigateToDiaryDetail = {
                    navigator.navigateToDiaryDetail(
                        plantId = it,
                        navOption = navOptions {
                            popUpTo<RouteModel.Home> {
                                inclusive = false
                            }
                        }
                    )
                }
            )
            diaryDetailScreen(

            )
            profileNavGraph(
                onClickBack = navigator::popBackStack,
                onClickSetting = navigator::navigateToSettings,
                onClickFAQ = { navigator.navigateToWebLink(RouteModel.WebLink.Link.QNA) },
                onClickPolicy = navigator::navigateToPolicy
            )
            settingsNavGraph(
                onClickBack = navigator::popBackStack,
                navigateToLogin = navigator::navigateToLoginAndClearBackStack,
                onClickDeleteAccount = navigator::navigateToUserDelete
            )
            userDeleteNavGraph(
                onClickBack = navigator::popBackStack,
                navigateToLogin = navigator::navigateToLoginAndClearBackStack
            )
            policyNavGraph(
                onClickBack = navigator::popBackStack,
                navigateToWebLink = navigator::navigateToWebLink
            )
            webLinkScreen(
                onClickClose = navigator::popBackStack
            )
        }
    }
}