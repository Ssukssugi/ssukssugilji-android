package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.model.LoginType
import com.sabo.core.model.SocialLogin

interface LoginRepository {
    suspend fun requestNaverLogin(token: String): Result<SocialLogin>
    suspend fun requestKakaoLogin(token: String): Result<SocialLogin>
    suspend fun requestGoogleLogin(token: String): Result<SocialLogin>
    suspend fun applyTermsAgreement(
        type: LoginType,
        isMarketingAgree: Boolean,
        socialId: String,
        emailAddress: String,
    ): Result<Unit>
}