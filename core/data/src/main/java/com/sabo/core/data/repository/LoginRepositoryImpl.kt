package com.sabo.core.data.repository

import com.sabo.core.data.handleResult
import com.sabo.core.domain.Result
import com.sabo.core.domain.model.SocialLogin
import com.sabo.core.domain.repository.LoginRepository
import com.sabo.core.domain.model.LoginType
import com.sabo.core.network.model.request.ApplyTermsAgreementRequest
import com.sabo.core.network.model.request.SocialLoginRequest
import com.sabo.core.network.service.LoginService
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun requestNaverLogin(token: String): Result<SocialLogin> = handleResult(
        execute = {
            val request = SocialLoginRequest(
                accessToken = token,
                loginType = LoginType.NAVER
            )
            loginService.requestSocialLogin(request)
        },
        transform = {
            SocialLogin(it.isRegistered, it.existInfo)
        }
    )

    override suspend fun requestKakaoLogin(token: String): Result<SocialLogin> = handleResult(
        execute = {
            val request = SocialLoginRequest(
                accessToken = token,
                loginType = LoginType.KAKAO
            )
            loginService.requestSocialLogin(request)
        },
        transform = {
            SocialLogin(it.isRegistered, it.existInfo)
        }
    )

    override suspend fun requestGoogleLogin(token: String): Result<SocialLogin> = handleResult(
        execute = {
            val request = SocialLoginRequest(
                accessToken = token,
                loginType = LoginType.GOOGLE
            )
            loginService.requestSocialLogin(request)
        },
        transform = {
            SocialLogin(it.isRegistered, it.existInfo)
        }
    )

    override suspend fun applyTermsAgreement(
        token: String,
        type: LoginType,
        isMarketingAgree: Boolean
    ): Result<Unit> = handleResult(
        execute = {
            val request = ApplyTermsAgreementRequest(
                accessToken = token,
                loginType = type,
                termsAgreement = ApplyTermsAgreementRequest.TermsAgreement(marketing = isMarketingAgree)
            )
            loginService.requestTermsAgreeApply(request)
        },
        transform = {}
    )
}