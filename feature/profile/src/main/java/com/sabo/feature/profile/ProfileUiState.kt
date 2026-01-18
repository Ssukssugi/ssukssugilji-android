package com.sabo.feature.profile

import com.sabo.core.model.NetworkErrorEvent

data class ProfileUiState(
    val profileContent: ProfileContent = ProfileContent.Loading,
)

sealed interface ProfileContent {
    data object Loading : ProfileContent
    data class Data(val name: String) : ProfileContent
    data class NetworkError(val error: NetworkErrorEvent) : ProfileContent
}

sealed interface ProfileEvent {
    data object ProfileUpdated : ProfileEvent
}
