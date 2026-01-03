package com.sabo.feature.profile.changeprofile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sabo.core.data.handle
import com.sabo.core.data.repository.ProfileRepository
import com.sabo.core.navigator.model.ChangeProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChangeProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val profileRepository: ProfileRepository
) : ContainerHost<ChangeProfileUiState, ChangeProfileSideEffect>, ViewModel() {

    private val route = savedStateHandle.toRoute<ChangeProfile>()
    private val textFieldState = TextFieldState(initialText = route.name)
    override val container: Container<ChangeProfileUiState, ChangeProfileSideEffect> = container(
        ChangeProfileUiState(
            nickname = route.name,
            textFieldState = textFieldState
        )
    )

    val isSaveEnabled = snapshotFlow { textFieldState.text }
            .map { text ->
                val textString = text.toString()
                textString.isNotEmpty() && textString.matches(Regex("^[가-힣a-zA-Z0-9]+$"))
            }.stateIn(
                initialValue = false,
                started = SharingStarted.WhileSubscribed(5_000),
                scope = viewModelScope
            )

    fun changeNickname() = intent {
        if (state.isLoading) return@intent

        reduce { state.copy(isLoading = true) }

        val nickname = state.textFieldState.text.toString()
        profileRepository.changeUserProfile(nickname).handle(
            onSuccess = {
                postSideEffect(ChangeProfileSideEffect.FinishWithResult)
            },
            onError = {
                reduce { state.copy(errorState = ChangeProfileUiState.ErrorType.DUPLICATED) }
            },
            onFinish = {
                reduce { state.copy(isLoading = false) }
            }
        )
    }
}