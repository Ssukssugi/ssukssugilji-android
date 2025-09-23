package com.sabo.feature.main.component

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.detail.navigation.navigateToDiaryDetail
import com.sabo.feature.diary.gallery.navigation.navigateToGallery
import com.sabo.feature.diary.plantadd.categorySearch.navigation.navigateToCategorySearch
import com.sabo.feature.diary.plantadd.navigation.navigateToPlantAdd
import com.sabo.feature.diary.write.navigation.navigateToDiaryWrite
import com.sabo.feature.home.navigation.navigateToHome
import com.sabo.feature.login.navigateToLogin
import com.sabo.feature.profile.navigation.navigateToPolicy
import com.sabo.feature.profile.navigation.navigateToProfile
import com.sabo.feature.profile.navigation.navigateToSettings
import com.sabo.feature.signup.navigateToSignUp
import com.sabo.feature.web.navigation.navigateToWebLink

class MainNavigator(
    val navController: NavHostController
) {
    val startDestination = RouteModel.Login

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigateToSignUp() {
        navController.navigateToSignUp()
    }

    fun navigateToHome() {
        navController.navigateToHome()
    }

    fun navigateToPlantAdd() {
        navController.navigateToPlantAdd()
    }

    fun navigateToCategorySearch(keyword: String) {
        navController.navigateToCategorySearch(keyword)
    }

    fun navigateToGallery() {
        navController.navigateToGallery()
    }

    fun navigateToDiaryWrite(imageUri: Uri) {
        navController.navigateToDiaryWrite(imageUri = imageUri)
    }

    fun navigateToDiaryDetail(plantId: Long, navOption: NavOptions? = null) {
        navController.navigateToDiaryDetail(plantId = plantId, navOptions = navOption)
    }

    fun navigateToProfile() {
        navController.navigateToProfile()
    }

    fun navigateToPolicy() {
        navController.navigateToPolicy()
    }

    fun navigateToSettings() {
        navController.navigateToSettings()
    }

    fun navigateToWebLink(link: RouteModel.WebLink.Link) {
        navController.navigateToWebLink(link)
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}