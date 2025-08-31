package com.sabo.core.data.repository

import com.sabo.core.data.handleResult
import com.sabo.core.network.service.ProfileService
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
    private val profileService: ProfileService
): ProfileRepository {
    override suspend fun getUserProfile() = handleResult(
        execute = { profileService.getUserProfile() },
        transform = { it }
    )
}