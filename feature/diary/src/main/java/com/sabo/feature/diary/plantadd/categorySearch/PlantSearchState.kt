package com.sabo.feature.diary.plantadd.categorySearch

import androidx.compose.foundation.text.input.TextFieldState

data class PlantSearchState(
    val textFieldState: TextFieldState = TextFieldState(),
    val searchResult: List<String> = emptyList()
)