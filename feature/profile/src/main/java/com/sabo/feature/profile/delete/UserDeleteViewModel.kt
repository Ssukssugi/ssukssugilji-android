package com.sabo.feature.profile.delete

import androidx.lifecycle.ViewModel
import com.sabo.core.data.handle
import com.sabo.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class UserDeleteViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ContainerHost<UserDeleteUiState, UserDeleteUiEvent>, ViewModel() {

    override val container: Container<UserDeleteUiState, UserDeleteUiEvent> = container(initialState = UserDeleteUiState)

    fun onClickDelete() = intent {
        postSideEffect(UserDeleteUiEvent.ShowAlertDialog)
    }

    fun deleteUser() = intent {
        profileRepository.deleteUser().handle(
            onSuccess = {
                delay(300)
                postSideEffect(UserDeleteUiEvent.UserDeleted)
            }
        )
    }
}