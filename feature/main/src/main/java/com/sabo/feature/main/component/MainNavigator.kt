package com.sabo.feature.main.component

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.sabo.core.navigator.model.DiaryEdit
import com.sabo.core.navigator.model.GrowthVariation
import com.sabo.core.navigator.model.Diary
import com.sabo.core.navigator.model.Login
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.core.navigator.model.WebLink
import com.sabo.feature.diary.detail.navigation.navigateToDiaryDetail
import com.sabo.feature.diary.gallery.navigation.navigateToGallery
import com.sabo.feature.diary.plantadd.categorySearch.navigation.navigateToCategorySearch
import com.sabo.feature.diary.plantadd.navigation.navigateToPlantAdd
import com.sabo.feature.diary.plantadd.navigation.navigateToPlantEdit
import com.sabo.feature.diary.write.navigation.navigateToDiaryEdit
import com.sabo.feature.diary.write.navigation.navigateToDiaryWrite
import com.sabo.feature.home.navigation.navigateToHome
import com.sabo.feature.profile.navigation.navigateToChangeProfile
import com.sabo.feature.town.navigateToTown
import com.sabo.feature.profile.navigation.navigateToPolicy
import com.sabo.feature.profile.navigation.navigateToProfile
import com.sabo.feature.profile.navigation.navigateToSettings
import com.sabo.feature.profile.navigation.navigateToUserDelete
import com.sabo.feature.signup.navigateToSignUp
import com.sabo.feature.town.mygrowth.main.navigateToMyGrowth
import com.sabo.feature.town.mygrowth.posting.navigateToGrowthSelectPlant
import com.sabo.feature.town.mygrowth.variation.navigateToGrowthVariation
import com.sabo.feature.web.navigation.navigateToWebLink

class MainNavigator(
    val navController: NavHostController
) {

    fun navigateToLoginAndClearBackStack() {
        navController.navigate(
            route = Login,
            navOptions = navOptions {
                popUpTo(0) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = false
            }
        )
    }

    fun navigateToSignUp() {
        navController.navigateToSignUp()
    }

    fun navigateToHome(
        navOptions: NavOptions = navOptions {
            popUpTo<Diary> {
                inclusive = true
            }
            launchSingleTop = true
        }
    ) {
        navController.navigateToHome(navOptions = navOptions)
    }

    fun navigateToTown(navOptions: NavOptions? = null) {
        navController.navigateToTown(navOptions = navOptions)
    }

    fun navigateToPlantAdd() {
        navController.navigateToPlantAdd()
    }

    fun navigateToPlantEdit(route: PlantAddEdit.PlantEdit) {
        navController.navigateToPlantEdit(route)
    }

    fun navigateToCategorySearch(keyword: String) {
        navController.navigateToCategorySearch(keyword)
    }

    fun navigateToGallery(plantId: Long, navOption: NavOptions? = null) {
        navController.navigateToGallery(plantId = plantId, navOptions = navOption)
    }

    fun navigateToDiaryWrite(plantId: Long, imageUri: Uri) {
        navController.navigateToDiaryWrite(plantId = plantId, imageUri = imageUri)
    }

    fun navigateToDiaryDetail(plantId: Long, diaryId: Long, navOption: NavOptions? = null) {
        navController.navigateToDiaryDetail(plantId = plantId, diaryId = diaryId, navOptions = navOption)
    }

    fun navigateToDiaryEdit(route: DiaryEdit, navOption: NavOptions? = null) {
        navController.navigateToDiaryEdit(route)
    }

    fun navigateToMyGrowths() {
        navController.navigateToMyGrowth()
    }

    fun navigateToSelectGrowthPlant() {
        navController.navigateToGrowthSelectPlant()
    }

    fun navigateToGrowthVariation(route: GrowthVariation) {
        navController.navigateToGrowthVariation(route = route)
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

    fun navigateToUserDelete() {
        navController.navigateToUserDelete()
    }

    fun navigateToWebLink(link: WebLink.Link) {
        navController.navigateToWebLink(link)
    }

    fun navigateToChangeProfile(name: String) {
        navController.navigateToChangeProfile(name)
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