package com.sabo.core.network.model.response

import com.sabo.core.model.PlantEnvironmentPlace
import kotlinx.serialization.Serializable

@Serializable
data class GetPlantProfile(
    val name: String,
    val plantCategory: String,
    val plantImage: String,
    val shine: Int?,
    val place: PlantEnvironmentPlace
)
