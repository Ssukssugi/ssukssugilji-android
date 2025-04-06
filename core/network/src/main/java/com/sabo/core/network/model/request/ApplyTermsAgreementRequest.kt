package com.sabo.core.network.model.request

import com.sabo.core.domain.model.LoginType
import kotlinx.serialization.Serializable

@Serializable
data class ApplyTermsAgreementRequest(
    val accessToken: String,
    val loginType: LoginType,
    val termsAgreement: TermsAgreement
) {
    @Serializable
    data class TermsAgreement(
        val marketing: Boolean
    )
}
