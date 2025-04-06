package com.sabo.core.network.model.request

import com.sabo.core.domain.model.LoginType
import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginRequest(
    val accessToken: String,
    val loginType: LoginType
)
