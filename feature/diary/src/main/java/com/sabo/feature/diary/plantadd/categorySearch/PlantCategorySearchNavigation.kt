package com.sabo.feature.diary.plantadd.categorySearch

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sabo.core.navigator.RouteModel

fun NavController.navigateToCategorySearch(keyword: String) {
    navigate(route = RouteModel.CategorySearch(keyword))
}

fun NavController.popBackStackWithResult(category: String) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(RouteModel.CategorySearch.EXTRA_KEY, category)
    popBackStack<RouteModel.CategorySearch>(inclusive = false)
}

fun NavGraphBuilder.categorySearchScreen(
    onClickBack: () -> Unit,
    onClickCategory: (String) -> Unit
) {
    composable<RouteModel.CategorySearch> { entry ->
        val extra = entry.toRoute<RouteModel.CategorySearch>().keyword
        PlantCategorySearchScreen(
            viewModel = hiltViewModel<PlantCategorySearchViewModel, PlantCategorySearchViewModel.Factory> { factory ->
                factory.create(extra)
            },
            onClickBack = onClickBack,
            onClickCategory = onClickCategory
        )
    }
}