package com.sabo.feature.diary.plantadd

import androidx.compose.foundation.text.input.TextFieldState
import com.sabo.feature.diary.plantadd.LightAmount.Companion.NOT_SET

sealed interface PlantAddState {
    data class Input(
        val textFieldState: TextFieldState = TextFieldState(),
        val plantCategory: PlantCategory? = null,
        val lightAmount: LightAmount = NOT_SET,
        val place: PlantPlace? = null,
    ) : PlantAddState

    data object SaveSuccess : PlantAddState
}

data class PlantCategory(
    val id: Long,
    val name: String,
)

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

