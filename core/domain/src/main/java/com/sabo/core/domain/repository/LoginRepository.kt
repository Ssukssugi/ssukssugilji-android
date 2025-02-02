package com.sabo.core.domain.repository

import com.sabo.core.domain.Result
import com.sabo.core.domain.model.SocialLogin

interface LoginRepository {
    suspend fun requestNaverLogin(token: String): Result<SocialLogin>
}