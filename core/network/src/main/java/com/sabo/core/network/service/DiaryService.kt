package com.sabo.core.network.service

import com.sabo.core.network.model.request.SaveNewPlantRequest
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
}