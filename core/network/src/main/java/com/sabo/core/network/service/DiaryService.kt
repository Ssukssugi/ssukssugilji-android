package com.sabo.core.network.service

import com.sabo.core.network.model.request.SaveNewPlantRequest
import com.sabo.core.network.model.response.GetMyPlant
import com.sabo.core.network.model.response.GetPlantDiaries
import com.sabo.core.network.model.response.GetPlantProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryService {

    @POST("/api/v1/plants")
    suspend fun saveNewPlant(
        @Body request: SaveNewPlantRequest
    ): Response<Unit>

    @GET("/api/v1/plant-categories/search")
    suspend fun getPlantCategories(
        @Query("keyword") keyword: String
    ): Response<List<String>>

    @GET("/api/v1/plants")
    suspend fun getMyPlants(): Response<List<GetMyPlant>>

    @GET("api/v1/plants/profile")
    suspend fun getPlantProfile(
        @Query("plantId") plantId: Long
    ): Response<GetPlantProfile>

    @GET("/api/v1/diaries/by-month")
    suspend fun getPlantDiaries(
        @Query("plantId") plantId: Long
    ): Response<GetPlantDiaries>
}