package com.sabo.feature.diary.plantadd.categorySearch

import androidx.compose.foundation.text.input.TextFieldState

data class PlantSearchState(
    val textFieldState: TextFieldState = TextFieldState(),
    val searchResult: List<PlantResult> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false
)

data class PlantResult(
    val imageUrl: String,
    val category: String
)