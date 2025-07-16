package com.sabo.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveNewPlantRequest(
    val name: String,
    val plantCategory: String,
    val plantEnvironment: PlantEnvironment
) {
    @Serializable
    data class PlantEnvironment(
        val shine: Int,
        val place: Place
    ) {
        enum class Place {
            @SerialName("VERANDAH") VERANDAH,
            @SerialName("WINDOW") WINDOW,
            @SerialName("HALLWAY") HALLWAY,
            @SerialName("LIVINGROOM") LIVINGROOM,
            @SerialName("ROOM") ROOM,
            @SerialName("ETC") ETC
        }
    }
}
