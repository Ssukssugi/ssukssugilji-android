package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetMyPlant(
    val plants: List<Plant>
) {
    @Serializable
    data class Plant(
        val plantId: Long,
        val name: String,
        val plantCategory: String,
        val image: String? = null
    )
}
