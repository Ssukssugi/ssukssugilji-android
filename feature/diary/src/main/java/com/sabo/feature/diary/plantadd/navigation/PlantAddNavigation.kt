package com.sabo.feature.diary.plantadd.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sabo.core.navigator.model.CategorySearch
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.core.navigator.toolkit.navTypeOf
import com.sabo.core.navigator.toolkit.slideInFromEnd
import com.sabo.core.navigator.toolkit.slideOutToEnd
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.diary.plantadd.PlantAddRoute
import com.sabo.feature.diary.plantadd.PlantAddViewModel
import kotlin.reflect.typeOf

fun NavController.navigateToPlantAdd() {
    this.navigate(PlantAddEdit.PlantAdd)
}

fun NavController.navigateToPlantEdit(route: PlantAddEdit.PlantEdit) {
    this.navigate(route)
}

fun NavGraphBuilder.plantAddNavGraph(
    onClickCategory: (String) -> Unit,
    onClickHome: () -> Unit,
    onClickDiary: (Long) -> Unit,
    onClickBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    composable<PlantAddEdit.PlantAdd>(
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) { entry ->
        val viewModel = hiltViewModel<PlantAddViewModel>()

        val selectedCategory by entry.savedStateHandle
            .getStateFlow<String?>(CategorySearch.EXTRA_KEY, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(selectedCategory) {
            selectedCategory?.let {
                viewModel.onSelectedPlantCategory(it)
                entry.savedStateHandle[CategorySearch.EXTRA_KEY] = null
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

    composable<PlantAddEdit.PlantEdit>(
        typeMap = mapOf(
            typeOf<PlantAddEdit.PlantEdit>() to navTypeOf<PlantAddEdit.PlantEdit>()
        ),
        enterTransition = slideInFromEnd(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToEnd()
    ) { entry ->
        val viewModel = hiltViewModel<PlantAddViewModel>()

        val selectedCategory by entry.savedStateHandle
            .getStateFlow<String?>(CategorySearch.EXTRA_KEY, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(selectedCategory) {
            selectedCategory?.let {
                viewModel.onSelectedPlantCategory(it)
                entry.savedStateHandle[CategorySearch.EXTRA_KEY] = null
            }
        }

        PlantAddRoute(
            viewModel = viewModel,
            onClickBack = onClickBack,
            onClickCategory = onClickCategory,
            onClickHome = onClickHome,
            onClickDiary = onClickDiary,
            navigateToHomeAfterPlantUpdated = navigateToHome
        )
    }
}