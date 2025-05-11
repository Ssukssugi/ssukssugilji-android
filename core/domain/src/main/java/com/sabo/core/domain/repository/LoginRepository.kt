package com.sabo.core.domain.repository

import com.sabo.core.domain.Result
import com.sabo.core.domain.model.LoginType
import com.sabo.core.domain.model.SocialLogin

interface LoginRepository {
    suspend fun requestNaverLogin(token: String): Result<SocialLogin>
    suspend fun requestKakaoLogin(token: String): Result<SocialLogin>
    suspend fun requestGoogleLogin(token: String): Result<SocialLogin>
    suspend fun applyTermsAgreement(token: String, type: LoginType, isMarketingAgree: Boolean): Result<Unit>
}