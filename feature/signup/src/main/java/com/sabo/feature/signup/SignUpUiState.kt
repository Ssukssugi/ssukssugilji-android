package com.sabo.feature.signup

sealed interface SignUpUiState {
    data class CreateNickname(
        val nickname: String = "",
        val errorState: ErrorState = ErrorState.NONE
    ) : SignUpUiState {
        enum class ErrorState {
            NONE, INVALID_FORMAT, DUPLICATED
        }
    }
}