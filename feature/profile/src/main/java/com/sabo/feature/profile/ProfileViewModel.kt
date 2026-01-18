package com.sabo.feature.profile

import androidx.lifecycle.ViewModel
import com.sabo.core.data.handle
import com.sabo.core.data.repository.ProfileRepository
import com.sabo.core.model.NetworkErrorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ContainerHost<ProfileUiState, ProfileEvent>, ViewModel() {

    override val container: Container<ProfileUiState, ProfileEvent> = container(
        initialState = ProfileUiState(
            profileContent = ProfileContent.Loading
        ),
        onCreate = { loadProfile() }
    )

    private fun loadProfile() = intent {
        reduce { state.copy(profileContent = ProfileContent.Loading) }
        profileRepository.getUserProfile().handle(
            onSuccess = {
                reduce { state.copy(profileContent = ProfileContent.Data(name = it.nickname)) }
            },
            onError = {
                reduce { state.copy(profileContent = ProfileContent.NetworkError(NetworkErrorEvent.NoInternet)) }
            }
        )
    }

    fun reloadProfile() = intent {
        loadProfile()
        postSideEffect(ProfileEvent.ProfileUpdated)
    }

    fun onRetryClicked() = intent {
        loadProfile()
    }
}