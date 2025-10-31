package com.sabo.feature.diary.detail

import com.sabo.core.designsystem.component.CareTypeIcon
import com.sabo.core.navigator.model.DiaryEdit
import com.sabo.core.network.model.response.GetPlantDiaries

data class DiaryDetailUiState(
    val isLoading: Boolean,
    val profileImage: String = "",
    val nickname: String = "",
    val diaries: List<GetPlantDiaries.MonthlyContent.Diary> = emptyList(),
    val selectedDiaryIndex: Int = 0,
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
    data class NavigateToEditDiary(val route: DiaryEdit): DiaryDetailUiEvent
    data object ShowDeleteDiarySnackBar: DiaryDetailUiEvent
}
