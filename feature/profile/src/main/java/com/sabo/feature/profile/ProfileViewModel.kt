package com.sabo.feature.profile

import androidx.lifecycle.ViewModel
import com.sabo.core.data.handle
import com.sabo.core.data.repository.ProfileRepository
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
            name = ""
        ),
        onCreate = { loadProfile() }
    )

    private fun loadProfile() = intent {
        profileRepository.getUserProfile().handle(
            onSuccess = {
                reduce { state.copy(name = it.nickname) }
            },
        )
    }
}