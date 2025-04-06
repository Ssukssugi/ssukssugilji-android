package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckNicknameResponse(
    val available: Boolean
)
