package com.sabo.core.network.service

import com.sabo.core.network.model.request.ApplyTermsAgreementRequest
import com.sabo.core.network.model.request.SocialLoginRequest
import com.sabo.core.network.model.response.SocialLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/api/v1/auth/social-login")
    suspend fun requestSocialLogin(
        @Body body: SocialLoginRequest
    ): Response<SocialLoginResponse>

    @POST("/api/v1/auth/sign-up")
    suspend fun requestTermsAgreeApply(
        @Body body: ApplyTermsAgreementRequest
    ): Response<Unit>
}