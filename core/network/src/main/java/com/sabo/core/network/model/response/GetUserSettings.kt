package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetUserSettings(
    val receiveServiceNoti: Boolean,
    val agreeToReceiveMarketing: Boolean
)