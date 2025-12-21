package com.sabo.feature.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.Diary
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.core.navigator.toolkit.tabFadeIn
import com.sabo.core.navigator.toolkit.tabFadeOut
import com.sabo.feature.home.HomeScreen
import com.sabo.feature.home.HomeViewModel

fun NavController.navigateToHome(navOptions: NavOptions) {
    this.navigate(Diary, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    navigateToPlantAdd: () -> Unit,
    navigateToDiaryDetail: (Long, Long) -> Unit,
    navigateToPlantEdit: (PlantAddEdit.PlantEdit) -> Unit,
    onSelectedPlantIdChange: (Long?) -> Unit = {},
) {
    composable<Diary>(
        enterTransition = tabFadeIn(),
        exitTransition = tabFadeOut()
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
            navigateToPlantAdd = navigateToPlantAdd,
            navigateToDiaryDetail = navigateToDiaryDetail,
            navigateToPlantEdit = navigateToPlantEdit,
            onSelectedPlantIdChange = onSelectedPlantIdChange,
        )
    }
}