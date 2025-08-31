package com.sabo.core.network.service

import com.sabo.core.network.model.response.GetUserProfile
import retrofit2.Response
import retrofit2.http.GET

interface ProfileService {
    @GET("/api/v1/users/profile")
    suspend fun getUserProfile(): Response<GetUserProfile>
}