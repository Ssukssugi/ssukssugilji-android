package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetPlantCategories(
    val name : String,
    val imageUrl: String
)
