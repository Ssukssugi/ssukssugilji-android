package com.sabo.feature.home.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.Home
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.feature.home.HomeScreen
import com.sabo.feature.home.HomeViewModel

fun NavController.navigateToHome(navOptions: NavOptions) {
    this.navigate(Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    navigateToGallery: () -> Unit,
    navigateToPlantAdd: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToDiaryDetail: (Long, Long) -> Unit,
    navigateToPlantEdit: (PlantAddEdit.PlantEdit) -> Unit
) {
    composable<Home>(
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        exitTransition = { ExitTransition.None }
    ) { entry ->
        val viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()

        val editedPlantId by entry.savedStateHandle
            .getStateFlow<Long?>(PlantAddEdit.PlantEdit.EXTRA_PLANT_ID, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(editedPlantId) {
            editedPlantId?.let {
                viewModel.refreshPlantContent()
                entry.savedStateHandle[PlantAddEdit.PlantEdit.EXTRA_PLANT_ID] = null
            }
        }

        HomeScreen(
            viewModel = viewModel,
            navigateToGallery = navigateToGallery,
            navigateToPlantAdd = navigateToPlantAdd,
            navigateToProfile = navigateToProfile,
            navigateToDiaryDetail = navigateToDiaryDetail,
            navigateToPlantEdit = navigateToPlantEdit
        )
    }
}