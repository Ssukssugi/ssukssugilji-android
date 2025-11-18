package com.sabo.feature.town.mygrowth.posting

import com.sabo.core.navigator.model.GrowthVariation

data class SelectPlantState(
    val isLoading: Boolean = false,
    val plantList: List<Plant>
)

data class Plant(
    val id: Long,
    val image: String,
    val nickname: String,
    val category: String,
    val isSelected: Boolean,
    val enabled: Boolean
)

sealed interface SelectPlantSideEffect {
    data class NavigateToGrowthVariation(val route: GrowthVariation) : SelectPlantSideEffect
}