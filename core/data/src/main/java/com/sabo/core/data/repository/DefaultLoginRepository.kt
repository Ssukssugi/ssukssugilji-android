package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.data.handleResult
import com.sabo.core.model.LoginType
import com.sabo.core.model.SocialLogin
import com.sabo.core.data.provider.AuthTokenProvider
import com.sabo.core.network.model.request.SocialLoginRequest
import com.sabo.core.network.model.request.ApplyTermsAgreementRequest
import com.sabo.core.network.model.response.SocialLoginResponse
import com.sabo.core.network.service.LoginService
import javax.inject.Inject

class DefaultLoginRepository @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun requestNaverLogin(token: String): Result<SocialLogin> = handleResult(
        execute = {
            loginService.requestSocialLogin(
                SocialLoginRequest(
                    accessToken = token,
                    loginType = LoginType.NAVER
                )
            )
        },
        transform = { response ->
            SocialLogin(
                isRegistered = response.isRegistered,
                existInfo = response.existInfo
            )
        }
    )

    override suspend fun requestKakaoLogin(token: String): Result<SocialLogin> = handleResult(
        execute = {
            loginService.requestSocialLogin(
                SocialLoginRequest(
                    accessToken = token,
                    loginType = LoginType.KAKAO
                )
            )
        },
        transform = { response ->
            SocialLogin(
                isRegistered = response.isRegistered,
                existInfo = response.existInfo
            )
        }
    )

    override suspend fun requestGoogleLogin(token: String): Result<SocialLogin> = handleResult(
        execute = {
            loginService.requestSocialLogin(
                SocialLoginRequest(
                    accessToken = token,
                    loginType = LoginType.GOOGLE
                )
            )
        },
        transform = { response ->
            SocialLogin(
                isRegistered = response.isRegistered,
                existInfo = response.existInfo
            )
        }
    )

    override suspend fun applyTermsAgreement(
        token: String,
        type: LoginType,
        isMarketingAgree: Boolean
    ): Result<Unit> = handleResult(
        execute = {
            loginService.requestTermsAgreeApply(
                ApplyTermsAgreementRequest(
                    accessToken = token,
                    loginType = type,
                    termsAgreement = ApplyTermsAgreementRequest.TermsAgreement(
                        marketing = isMarketingAgree
                    )
                )
            )
        },
        transform = { }
    )
}