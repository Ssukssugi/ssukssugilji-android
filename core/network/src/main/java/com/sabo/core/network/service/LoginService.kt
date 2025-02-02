package com.sabo.core.network.service

import com.sabo.core.network.BaseResponse
import com.sabo.core.network.model.request.SocialLoginRequest
import com.sabo.core.network.model.response.SocialLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/api/v1/auth/social-login")
    suspend fun requestSocialLogin(
        @Body body: SocialLoginRequest
    ): BaseResponse<SocialLoginResponse>
}