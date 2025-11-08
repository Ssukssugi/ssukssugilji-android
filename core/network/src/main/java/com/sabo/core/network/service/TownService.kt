package com.sabo.core.network.service

import com.sabo.core.network.model.response.GetTownGrowth
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TownService {

    @GET("/api/v1/town/growth")
    suspend fun getTownGrowth(
        @Query("lastGrowthId") lastGrowthId: Long? = null
    ): Response<GetTownGrowth>

    @GET("/api/v1/growth")
    suspend fun getMyGrowth(): Response<GetTownGrowth>
}