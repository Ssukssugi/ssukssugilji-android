package com.sabo.feature.signup.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable


@Immutable
data class SignUpUiState(
    val step: SignUpStep = SignUpStep.NICKNAME,
    val nickname: TextFieldState = TextFieldState(),
    val nicknameErrorState: NicknameErrorState = NicknameErrorState.NONE,
    val age: Age? = null,
    val plantReason: Set<PlantReason> = emptySet(),
    val howKnown: Set<HowKnown> = emptySet()
) {
    enum class NicknameErrorState {
        NONE, INVALID_FORMAT, DUPLICATED
    }

    enum class Age {
        TWENTY, THIRTY, FORTY, FIFTY, SIXTY, OLDER
    }

    enum class PlantReason {
        HEALING, INTERIOR, FOOD, FEEL, ETC
    }

    enum class HowKnown {
        APPSTORE, BLOG, INSTAGRAM, FRIEND, ETC
    }
}

sealed interface SignUpEvent {

}