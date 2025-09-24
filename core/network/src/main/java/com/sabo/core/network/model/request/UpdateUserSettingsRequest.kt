package com.sabo.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserSettingsRequest(
    val key: UserSettingsKey,
    val value: Boolean
)

@Serializable
enum class UserSettingsKey {

    @SerialName("MARKETING")
    MARKETING,

    @SerialName("SERVICE_NOTI")
    SERVICE_NOTIFICATION
}
