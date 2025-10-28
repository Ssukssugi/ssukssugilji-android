package com.sabo.feature.profile.changeprofile

import androidx.compose.foundation.text.input.TextFieldState

data class ChangeProfileUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val textFieldState: TextFieldState = TextFieldState(initialText = "")
)

sealed interface ChangeProfileSideEffect {
    data object FinishWithResult : ChangeProfileSideEffect
}
