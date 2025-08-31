package com.sabo.feature.profile

data class ProfileUiState(
    val name: String,
)

sealed interface ProfileEvent {

}
