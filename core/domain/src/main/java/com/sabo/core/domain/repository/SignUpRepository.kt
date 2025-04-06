package com.sabo.core.domain.repository

import com.sabo.core.domain.Result
import com.sabo.core.domain.model.CheckNickname

interface SignUpRepository {
    suspend fun checkNicknameDuplicated(nickname: String): Result<CheckNickname>
    suspend fun applyUserDetails(
        nickname: String,
        ageGroup: Long?,
        plantReason: Set<String>?,
        signUpPath: Set<String>?
    ): Result<Unit>
}