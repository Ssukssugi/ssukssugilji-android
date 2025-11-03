package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetTownGrowth(
    val contents: List<GrowthContent>
) {
    @Serializable
    data class GrowthContent(
        val growthId: Long,
        val owner: Owner,
        val plant: Plant,
        val before: GrowthImage,
        val after: GrowthImage
    )

    @Serializable
    data class Owner(
        val userId: Long,
        val nickname: String
    )

    @Serializable
    data class Plant(
        val name: String,
        val image: String
    )

    @Serializable
    data class GrowthImage(
        val imageUrl: String,
        val date: String
    )
}
