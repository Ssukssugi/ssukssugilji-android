package com.sabo.feature.diary.plantadd.categorySearch.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sabo.core.navigator.model.CategorySearch
import com.sabo.feature.diary.plantadd.categorySearch.PlantCategorySearchScreen
import com.sabo.feature.diary.plantadd.categorySearch.PlantCategorySearchViewModel

fun NavController.navigateToCategorySearch(keyword: String) {
    navigate(route = CategorySearch(keyword))
}

fun NavController.popBackStackWithResult(category: String) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(CategorySearch.EXTRA_KEY, category)
    popBackStack()
}

fun NavGraphBuilder.categorySearchScreen(
    onClickBack: () -> Unit,
    onClickCategory: (String) -> Unit
) {
    composable<CategorySearch> { entry ->
        val extra = entry.toRoute<CategorySearch>().keyword
        PlantCategorySearchScreen(
            viewModel = hiltViewModel<PlantCategorySearchViewModel, PlantCategorySearchViewModel.Factory> { factory ->
                factory.create(extra)
            },
            onClickBack = onClickBack,
            onClickCategory = onClickCategory
        )
    }
}