package com.sabo.feature.profile.changeprofile

import androidx.compose.foundation.text.input.TextFieldState

data class ChangeProfileUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val textFieldState: TextFieldState = TextFieldState(initialText = ""),
    val errorState: ErrorType = ErrorType.NONE
) {
    enum class ErrorType(val helper: String) {
        NONE(""),
        DUPLICATED("이미 사용중인 닉네임입니다.")
    }
}

sealed interface ChangeProfileSideEffect {
    data object FinishWithResult : ChangeProfileSideEffect
}
