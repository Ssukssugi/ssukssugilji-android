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
import com.sabo.core.navigator.model.Home
import com.sabo.core.navigator.model.Login
import com.sabo.core.navigator.model.WebLink
import com.sabo.feature.diary.detail.navigation.diaryDetailScreen
import com.sabo.feature.diary.gallery.navigation.galleryScreen
import com.sabo.feature.diary.plantadd.categorySearch.navigation.categorySearchScreen
import com.sabo.feature.diary.plantadd.categorySearch.navigation.popBackStackWithResult
import com.sabo.feature.diary.plantadd.navigation.plantAddNavGraph
import com.sabo.feature.diary.write.navigation.diaryWriteScreen
import com.sabo.feature.home.navigation.homeNavGraph
import com.sabo.feature.login.loginNavGraph
import com.sabo.feature.profile.navigation.changeProfileNavGraph
import com.sabo.feature.profile.navigation.policyNavGraph
import com.sabo.feature.profile.navigation.popBackStackWithResultProfileUpdated
import com.sabo.feature.profile.navigation.profileNavGraph
import com.sabo.feature.profile.navigation.settingsNavGraph
import com.sabo.feature.profile.navigation.userDeleteNavGraph
import com.sabo.feature.signup.signUpNavGraph
import com.sabo.feature.town.mygrowth.main.myGrowthScreen
import com.sabo.feature.town.mygrowth.posting.selectPlantScreen
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
            startDestination = Login
        ) {
            loginNavGraph(
                navigateToSignUp = navigator::navigateToSignUp,
                navigateToHome = {
                    navigator.navigateToHome(
                        navOptions = navOptions {
                            popUpTo<Login> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    )
                }
            )
            signUpNavGraph(
                onCompletedSignUp = {
                    navigator.navigateToHome(
                        navOptions = navOptions {
                            popUpTo<Login> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    )
                }
            )
            homeNavGraph(
                navigateToGallery = navigator::navigateToGallery,
                navigateToPlantAdd = navigator::navigateToPlantAdd,
                navigateToProfile = navigator::navigateToProfile,
                navigateToDiaryDetail = navigator::navigateToDiaryDetail,
                navigateToPlantEdit = navigator::navigateToPlantEdit,
                navigateToMyGrowths = navigator::navigateToMyGrowths
            )
            plantAddNavGraph(
                onClickCategory = navigator::navigateToCategorySearch,
                onClickHome = navigator::navigateToHome,
                onClickDiary = {
                    navigator.navigateToGallery(
                        navOption = navOptions {
                            popUpTo<Home> {
                                inclusive = false
                            }
                        }
                    )
                },
                onClickBack = navigator::popBackStack,
                navigateToHome = navigator::navigateToHome
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
                        diaryId = -1L,
                        navOption = navOptions {
                            popUpTo<Home> {
                                inclusive = false
                            }
                        }
                    )
                }
            )
            diaryDetailScreen(
                onClickBack = navigator::popBackStack,
                navigateToEditDiary = navigator::navigateToDiaryEdit,
                popUpWithResult = navigator::navigateToHome
            )
            myGrowthScreen(
                onClickBack = navigator::popBackStack,
                onClickPosting = navigator::navigateToSelectGrowthPlant
            )
            selectPlantScreen(
                onClickBack = navigator::popBackStack,
                onClickNext = {}
            )
            profileNavGraph(
                onClickBack = navigator::popBackStack,
                onClickSetting = navigator::navigateToSettings,
                onClickFAQ = { navigator.navigateToWebLink(WebLink.Link.QNA) },
                onClickPolicy = navigator::navigateToPolicy,
                onClickProfile = navigator::navigateToChangeProfile
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
            changeProfileNavGraph(
                onClickBack = navigator::popBackStack,
                onSucceedSave = navigator.navController::popBackStackWithResultProfileUpdated
            )
        }
    }
}