package com.sabo.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CareType {
    @SerialName("WATER")
    WATER,

    @SerialName("DIVIDING")
    DIVIDING,

    @SerialName("NUTRIENT")
    NUTRIENT,

    @SerialName("PRUNING")
    PRUNING
}