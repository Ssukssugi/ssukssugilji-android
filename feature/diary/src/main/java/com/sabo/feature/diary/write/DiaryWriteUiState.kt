package com.sabo.feature.diary.write

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import com.sabo.core.model.CareType
import java.time.LocalDate

data class DiaryWriteUiState(
    val isLoading: Boolean = false,
    val imageUri: Uri = Uri.EMPTY,
    val plants: List<PlantListItem> = emptyList(),
    val date: LocalDate = LocalDate.now(),
    val careTypes: List<CareTypeItem> = CareType.entries.map { CareTypeItem(it) },
    val content: TextFieldState = TextFieldState(),
    val isSaveLoading: Boolean = false,
    val isSaveSuccess: Boolean = false
)

data class PlantListItem(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val isSelected: Boolean = false
)

data class CareTypeItem(
    val type: CareType,
    val isSelected: Boolean = false
)

sealed interface DiaryWriteSideEffect {
    data class NavigateToDetail(val plantId: Long) : DiaryWriteSideEffect
    data class ShowSnackBar(val type: SnackBarType) : DiaryWriteSideEffect {
        enum class SnackBarType {
            PLANT_REQUIRED
        }
    }
}
