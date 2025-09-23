package com.sabo.feature.profile

data class SettingsUiState(
    val isLoading: Boolean = true,
    val serviceNotificationEnabled: Boolean = false,
    val marketingNotificationEnabled: Boolean = false,
)