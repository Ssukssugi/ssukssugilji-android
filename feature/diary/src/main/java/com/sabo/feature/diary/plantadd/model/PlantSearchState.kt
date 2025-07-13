package com.sabo.feature.diary.plantadd.model

sealed interface PlantSearchState {
    data class Success(val results: List<String>): PlantSearchState
    data object Loading: PlantSearchState
    data object Error: PlantSearchState
}