package com.sabo.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginRequest(
    val accessToken: String,
    val loginType: LoginType
) {
    @Serializable
    enum class LoginType {
        @SerialName("KAKAO")
        KAKAO,
        @SerialName("NAVER")
        NAVER,
        @SerialName("GOOGLE")
        GOOGLE
    }
}
