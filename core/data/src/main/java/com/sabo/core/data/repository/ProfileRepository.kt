package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.network.model.response.GetUserProfile
import com.sabo.core.network.model.response.GetUserSettings

interface ProfileRepository {
    suspend fun getUserProfile(): Result<GetUserProfile>

    suspend fun getUserSettings(): Result<GetUserSettings>
}