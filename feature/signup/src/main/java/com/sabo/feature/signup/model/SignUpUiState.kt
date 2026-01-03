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
    enum class NicknameErrorState(val helper: String) {
        NONE(""),
        INVALID_FORMAT("한글, 숫자, 영문 포함 12글자까지 입력할 수 있어요"),
        DUPLICATED("이미 사용 중인 닉네임이에요")
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
    data object OnCompletedSignUp : SignUpEvent
}