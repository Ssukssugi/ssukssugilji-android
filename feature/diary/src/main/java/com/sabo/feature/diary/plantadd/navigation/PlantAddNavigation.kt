package com.sabo.feature.diary.plantadd.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.diary.plantadd.PlantAddRoute
import com.sabo.feature.diary.plantadd.PlantAddViewModel

fun NavController.navigateToPlantAdd() {
    this.navigate(RouteModel.PlantAdd)
}

fun NavGraphBuilder.plantAddNavGraph(
    onClickCategory: (String) -> Unit,
    onClickHome: () -> Unit,
    onClickDiary: () -> Unit,
    onClickBack: () -> Unit
) {
    composable<RouteModel.PlantAdd> { entry ->
        val viewModel = hiltViewModel<PlantAddViewModel>()

        val selectedCategory by entry.savedStateHandle
            .getStateFlow<String?>(RouteModel.CategorySearch.EXTRA_KEY, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(selectedCategory) {
            selectedCategory?.let {
                viewModel.onSelectedPlantCategory(it)
                entry.savedStateHandle[RouteModel.CategorySearch.EXTRA_KEY] = null
            }
        }

        PlantAddRoute(
            viewModel = viewModel,
            onClickBack = onClickBack,
            onClickCategory = onClickCategory,
            onClickHome = onClickHome,
            onClickDiary = onClickDiary
        )
    }
}