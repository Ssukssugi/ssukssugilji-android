package com.sabo.feature.signup

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.domain.handle
import com.sabo.core.domain.repository.SignUpRepository
import com.sabo.feature.signup.model.AgeChip
import com.sabo.feature.signup.model.PlantReasonChip
import com.sabo.feature.signup.model.SignUpEvent
import com.sabo.feature.signup.model.SignUpStep
import com.sabo.feature.signup.model.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
) : ViewModel() {

    private val nickname = TextFieldState()

    private val _uiState = MutableStateFlow(SignUpUiState(nickname = nickname))
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SignUpEvent>(Channel.RENDEZVOUS)
    val event = _event.receiveAsFlow()

    fun checkIsNicknameDuplicated() {
        viewModelScope.launch {
            signUpRepository.checkNicknameDuplicated(nickname.text.toString()).handle(
                onSuccess = {
                    if (it.available) {
                        moveToNextStep()
                    }
                },
                onError = {
                }
            )
        }
    }

    fun onClickBackScreen() {
        val prev: SignUpStep = when (uiState.value.step) {
            SignUpStep.NICKNAME, SignUpStep.COMPLETED -> return
            SignUpStep.AGE -> SignUpStep.NICKNAME
            SignUpStep.PLANT_REASON -> SignUpStep.AGE
            SignUpStep.HOW_KNOWN -> SignUpStep.PLANT_REASON
        }
        _uiState.value = uiState.value.copy(step = prev)
    }

    fun moveToNextStep() {
        val next: SignUpStep = when (uiState.value.step) {
            SignUpStep.NICKNAME -> SignUpStep.AGE
            SignUpStep.AGE -> SignUpStep.PLANT_REASON
            SignUpStep.PLANT_REASON -> SignUpStep.HOW_KNOWN
            SignUpStep.HOW_KNOWN -> {
                applyUserDetails()
                return
            }

            SignUpStep.COMPLETED -> return
        }

        _uiState.value = uiState.value.copy(step = next)
    }

    fun selectAge(age: AgeChip) {
        _uiState.value = uiState.value.copy(
            age = SignUpUiState.Age.entries.find { it.name == age.age.name }
        )
    }

    fun selectPlantReason(item: PlantReasonChip) {
        val newSet = uiState.value.plantReason.toMutableSet().apply {
            if (removeIf { it.name == item.plantReason.name }.not()) {
                SignUpUiState.PlantReason.entries.find {
                    it.name == item.plantReason.name
                }?.let {
                    add(it)
                }
            }
        }
        _uiState.value = uiState.value.copy(
            plantReason = newSet
        )
    }

    private fun applyUserDetails() {
        val state = uiState.value
        viewModelScope.launch {
            signUpRepository.applyUserDetails(
                nickname = state.nickname.text.toString(),
                ageGroup = when (state.age) {
                    SignUpUiState.Age.TWENTY -> 20
                    SignUpUiState.Age.THIRTY -> 30
                    SignUpUiState.Age.FORTY -> 40
                    SignUpUiState.Age.FIFTY -> 50
                    SignUpUiState.Age.SIXTY -> 60
                    SignUpUiState.Age.OLDER -> 70
                    else -> null
                },
                plantReason = state.plantReason.map { it.name }.toSet().ifEmpty { null },
                signUpPath = state.howKnown.map { it.name }.toSet().ifEmpty { null }
            ).handle(
                onSuccess = {
                    _uiState.value = uiState.value.copy(
                        step = SignUpStep.COMPLETED
                    )
                },
                onError = {

                }
            )
        }
    }
}
