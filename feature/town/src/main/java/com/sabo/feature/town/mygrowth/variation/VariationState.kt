package com.sabo.feature.town.mygrowth.variation

import com.sabo.feature.town.mygrowth.variation.component.VariationImageType

data class VariationState(
    val isLoading: Boolean = false,
    val plantId: Long = -1L,
    val plantNickname: String = "",
    val beforeImage: HistoryImage? = null,
    val afterImage: HistoryImage? = null,
    val imageList: List<HistoryImage> = emptyList()
)

data class HistoryImage(
    val url: String,
    val selectedType: VariationImageType?,
    val diaryId: Long
)

sealed interface VariationSideEffect {
    data object NavigateToHomeWithSuccess : VariationSideEffect
}

