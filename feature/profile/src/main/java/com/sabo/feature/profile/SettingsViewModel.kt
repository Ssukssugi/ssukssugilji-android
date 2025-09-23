package com.sabo.feature.profile

import androidx.lifecycle.ViewModel
import com.sabo.core.data.handle
import com.sabo.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
    data object ShowLogoutDialog : SettingsEvent
    data object ShowDeleteAccountDialog : SettingsEvent
    data class ShowError(val message: String) : SettingsEvent
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ContainerHost<SettingsUiState, SettingsEvent>, ViewModel() {

    override val container: Container<SettingsUiState, SettingsEvent> = container(
        initialState = SettingsUiState(),
        onCreate = { loadNotificationSettings() }
    )

    private fun loadNotificationSettings() = intent {
        reduce { state.copy(isLoading = true) }

        profileRepository.getUserSettings().handle(
            onSuccess = { settings ->
                reduce {
                    state.copy(
                        isLoading = false,
                        serviceNotificationEnabled = settings.receiveServiceNoti,
                        marketingNotificationEnabled = settings.agreeToReceiveMarketing,
                    )
                }
            },
            onError = { throwable ->
                reduce {
                    state.copy(
                        isLoading = false,
                    )
                }
            }
        )
    }

    fun onLogoutClick() = intent {
        postSideEffect(SettingsEvent.ShowLogoutDialog)
    }

    fun onDeleteAccountClick() = intent {
        postSideEffect(SettingsEvent.ShowDeleteAccountDialog)
    }
}