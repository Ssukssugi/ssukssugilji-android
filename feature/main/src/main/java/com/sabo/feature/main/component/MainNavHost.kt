package com.sabo.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.SnackBarState
import com.sabo.core.navigator.model.Diary
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
import com.sabo.feature.town.mygrowth.variation.growthVariationScreen
import com.sabo.feature.town.townScreen
import com.sabo.feature.web.navigation.webLinkScreen

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowSuccessSnackBar: (SnackBarState) -> Unit,
    onSelectedPlantIdChange: (Long?) -> Unit = {}
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
                },
                navigateToPolicy = {
                    navigator.navigateToWebLink(WebLink.Link.POLICY)
                },
                navigateToPrivacy = {
                    navigator.navigateToWebLink(WebLink.Link.PRIVACY)
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
                navigateToPlantAdd = navigator::navigateToPlantAdd,
                navigateToDiaryDetail = navigator::navigateToDiaryDetail,
                navigateToPlantEdit = navigator::navigateToPlantEdit,
                onSelectedPlantIdChange = onSelectedPlantIdChange,
            )
            townScreen(
                navigateToMyGrowths = navigator::navigateToMyGrowths,
                navigateToDiaryWrite = navigator::navigateToPlantAdd,
            )
            plantAddNavGraph(
                onClickCategory = navigator::navigateToCategorySearch,
                onClickHome = navigator::navigateToHome,
                onClickDiary = {
                    navigator.navigateToGallery(
                        plantId = it,
                        navOption = navOptions {
                            popUpTo<Diary> {
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
                            popUpTo<Diary> {
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
                onClickNext = navigator::navigateToGrowthVariation
            )
            growthVariationScreen(
                onClickBack = navigator::popBackStack,
                onNavigateToHomeWithSuccess = {
                    navigator.navController.navigate(
                        route = Diary,
                        navOptions = navOptions {
                            popUpTo(Diary) {
                                inclusive = true
                            }
                        }
                    )
                    onShowSuccessSnackBar(
                        SnackBarState(
                            message = "쑥쑥마을에 소개가 완료되었어요!",
                            iconRes = R.drawable.icon_circle_check
                        )
                    )
                }
            )
            profileNavGraph(
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