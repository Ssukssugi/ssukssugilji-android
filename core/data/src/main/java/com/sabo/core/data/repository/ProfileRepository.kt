package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.network.model.response.GetUserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): Result<GetUserProfile>
}