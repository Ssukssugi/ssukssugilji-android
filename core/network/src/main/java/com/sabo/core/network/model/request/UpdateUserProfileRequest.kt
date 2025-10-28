package com.sabo.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    val nickname: String
)
