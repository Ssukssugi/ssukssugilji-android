package com.sabo.core.network.service

import com.sabo.core.network.model.request.UpdateUserSettingsRequest
import com.sabo.core.network.model.response.GetUserSettings
import com.sabo.core.network.model.response.GetUserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileService {
    @GET("/api/v1/users/profile")
    suspend fun getUserProfile(): Response<GetUserProfile>

    @GET("/api/v1/users/settings")
    suspend fun getUserSettings(): Response<GetUserSettings>

    @POST("/api/v1/users/settings")
    suspend fun updateUserSettings(
        @Body body: UpdateUserSettingsRequest
    ): Response<Unit>
}