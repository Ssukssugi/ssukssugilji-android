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
    val careType: CareType? = null,
    val content: TextFieldState = TextFieldState()
)

data class PlantListItem(
    val id: Long,
    val name: String,
    val imageUrl: String
)

sealed interface DiaryWriteSideEffect {

}
