package com.sabo.core.network.service

import com.sabo.core.network.model.request.UpdateUserProfileRequest
import com.sabo.core.network.model.request.UpdateUserSettingsRequest
import com.sabo.core.network.model.response.GetUserSettings
import com.sabo.core.network.model.response.GetUserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileService {
    @GET("/api/v1/users/profile")
    suspend fun getUserProfile(): Response<GetUserProfile>

    @GET("/api/v1/users/settings")
    suspend fun getUserSettings(): Response<GetUserSettings>

    @POST("/api/v1/users/settings")
    suspend fun updateUserSettings(
        @Body body: UpdateUserSettingsRequest
    ): Response<Unit>

    @DELETE("/api/v1/users")
    suspend fun deleteUser(): Response<Boolean>

    @PUT("/api/v1/users/profile")
    suspend fun updateUserProfile(
        @Body body: UpdateUserProfileRequest
    ): Response<Unit>
}