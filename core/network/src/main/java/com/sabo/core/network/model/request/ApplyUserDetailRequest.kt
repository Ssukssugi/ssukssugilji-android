package com.sabo.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplyUserDetailRequest(
    val nickname: String,
    val ageGroup: Long?,
    val plantReason: List<PlantReason>?,
    val signUpPath: List<SinUpPath>?
) {
    enum class PlantReason {
        @SerialName("HEALING")
        HEALING,

        @SerialName("INTERIOR")
        INTERIOR,

        @SerialName("FOOD")
        FOOD,

        @SerialName("FEEL")
        FEEL,

        @SerialName("ETC")
        ETC
    }

    enum class SinUpPath {
        @SerialName("APPSTORE")
        APPSTORE,

        @SerialName("BLOG")
        BLOG,

        @SerialName("INSTAGRAM")
        INSTAGRAM,

        @SerialName("FRIEND")
        FRIEND,

        @SerialName("ETC")
        ETC
    }
}
