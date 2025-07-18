package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.model.CheckNickname

interface SignUpRepository {
    suspend fun checkNicknameDuplicated(nickname: String): Result<CheckNickname>
    suspend fun applyUserDetails(
        nickname: String,
        ageGroup: Long?,
        plantReason: Set<String>?,
        signUpPath: Set<String>?
    ): Result<Unit>
}