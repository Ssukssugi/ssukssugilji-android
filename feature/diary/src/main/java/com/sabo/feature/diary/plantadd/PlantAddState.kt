package com.sabo.feature.diary.plantadd

import androidx.compose.foundation.text.input.TextFieldState
import com.sabo.feature.diary.plantadd.LightAmount.Companion.NOT_SET

sealed interface PlantAddState {
    data class Input(
        val mode: Mode,
        val textFieldState: TextFieldState = TextFieldState(),
        val plantCategory: String? = null,
        val lightAmount: LightAmount = NOT_SET,
        val place: PlantPlace? = null,
    ) : PlantAddState {
        sealed interface Mode {
            data object Add : Mode
            data class Edit(val plantId: Long) : Mode
        }
    }

    data object SaveSuccess : PlantAddState
}

sealed interface PlantAddSideEffect {
    data object UpdateSuccess : PlantAddSideEffect
}

data class LightAmount(
    val value: Int,
    val helperText: String
) {
    companion object {
        val NOT_SET = LightAmount(
            -1,
            ""
        )
        val NONE = LightAmount(
            0,
            "햇빛이 아예 없어요"
        )
        val LOW = LightAmount(
            1,
            "하루에 짧게 해가 들어요"
        )
        val MEDIUM = LightAmount(
            2,
            "적당히 해가 잘 들어요"
        )
        val HIGH = LightAmount(
            3,
            "햇빝이 아주 쨍쨍하게 들어요"
        )

        fun fromValue(value: Int): LightAmount {
            return when (value) {
                0 -> NONE
                1 -> LOW
                2 -> MEDIUM
                3 -> HIGH
                else -> NOT_SET
            }
        }
    }
}

enum class PlantPlace(val displayName: String) {
    VERANDA("베란다"),
    WINDOW("창가"),
    HALLWAY("복도"),
    LIVINGROOM("거실"),
    ROOM("방 안"),
    OTHER("기타")
}

