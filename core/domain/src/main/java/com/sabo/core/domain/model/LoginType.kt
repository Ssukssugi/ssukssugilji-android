package com.sabo.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LoginType(val text: String) {
    @SerialName("KAKAO")
    KAKAO("카카오"),

    @SerialName("NAVER")
    NAVER("네이버"),

    @SerialName("GOOGLE")
    GOOGLE("구글")
}