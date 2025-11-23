package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SaveNewPlant(
    val plantId: Long
)
