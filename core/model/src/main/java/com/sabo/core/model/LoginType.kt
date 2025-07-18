package com.sabo.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LoginType {
    @SerialName("KAKAO")
    KAKAO,

    @SerialName("NAVER")
    NAVER,

    @SerialName("GOOGLE")
    GOOGLE
}