package com.sabo.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CheckNicknameRequest(
    val nickname: String
)
