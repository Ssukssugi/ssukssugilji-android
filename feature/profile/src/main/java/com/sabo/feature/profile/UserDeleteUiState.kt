package com.sabo.feature.profile

data object UserDeleteUiState

sealed interface UserDeleteUiEvent {
    data object ShowAlertDialog : UserDeleteUiEvent
    data object UserDeleted : UserDeleteUiEvent
}
