package com.sabo.feature.diary.detail

import com.sabo.core.designsystem.component.CareTypeIcon

data class DiaryDetailUiState(
    val isLoading: Boolean,
    val profileImage: String = "",
    val nickname: String = "",
    val historyImages: List<String> = emptyList(),
    val content: Content = Content.Loading,
)

sealed interface Content {
    data object Loading : Content
    data class Success(
        val careTypes: List<CareTypeIcon> = emptyList(),
        val updatedAt: String = "",
        val diary: String = "",
        val image: String = "",
    ): Content
}

sealed interface DiaryDetailUiEvent {

}
