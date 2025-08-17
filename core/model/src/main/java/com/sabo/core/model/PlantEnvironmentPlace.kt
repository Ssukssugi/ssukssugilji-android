package com.sabo.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PlantEnvironmentPlace(val display: String) {
    @SerialName("VERANDAH")
    VERANDAH("베란다"),

    @SerialName("WINDOW")
    WINDOW("창가"),

    @SerialName("HALLWAY")
    HALLWAY("복도"),

    @SerialName("LIVINGROOM")
    LIVINGROOM("거실"),

    @SerialName("ROOM")
    ROOM("방안"),

    @SerialName("ETC")
    ETC("기타")
}