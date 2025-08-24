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
import com.sabo.feature.diary.gallery.galleryScreen
import com.sabo.feature.diary.plantadd.categorySearch.categorySearchScreen
import com.sabo.feature.diary.plantadd.categorySearch.popBackStackWithResult
import com.sabo.feature.diary.plantadd.navigation.plantAddNavGraph
import com.sabo.feature.home.navigation.homeNavGraph
import com.sabo.feature.login.loginNavGraph
import com.sabo.feature.signup.signUpNavGraph

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
            startDestination = navigator.startDestination
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
                navigateToPlantAdd = navigator::navigateToPlantAdd
            )
            plantAddNavGraph(
                onClickCategory = navigator::navigateToCategorySearch,
                onClickHome = navigator::navigateToHome,
                onClickDiary = { /* TODO: 일지 작성 하러 가기 추가*/}
            )
            categorySearchScreen(
                onClickBack = navigator::popBackStack,
                onClickCategory = navigator.navController::popBackStackWithResult
            )
            galleryScreen()
        }
    }
}