package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginResponse(
    val isRegistered: Boolean,
    val existInfo: Boolean
)
