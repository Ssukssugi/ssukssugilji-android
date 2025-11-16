package com.sabo.core.network.service

import com.sabo.core.network.model.request.SaveNewPlantRequest
import com.sabo.core.network.model.response.GetMyPlant
import com.sabo.core.network.model.response.GetPlantCategories
import com.sabo.core.network.model.response.GetPlantDiaries
import com.sabo.core.network.model.response.GetPlantProfile
import com.sabo.core.network.model.response.SaveNewDiary
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface DiaryService {

    @POST("/api/v1/plants")
    suspend fun saveNewPlant(
        @Body request: SaveNewPlantRequest
    ): Response<Unit>

    @PUT("/api/v1/plants")
    suspend fun updatePlant(
        @Query("plantId") plantId: Long,
        @Body request: SaveNewPlantRequest
    ): Response<Unit>

    @DELETE("/api/v1/plants")
    suspend fun deletePlant(
        @Query("plantId") plantId: Long
    ): Response<Unit>

    @GET("/api/v1/plant-categories/search")
    suspend fun getPlantCategories(
        @Query("keyword") keyword: String
    ): Response<List<GetPlantCategories>>

    @GET("/api/v1/plants")
    suspend fun getMyPlants(
        @Query("diaryCount") diaryCount: Boolean
    ): Response<GetMyPlant>

    @GET("api/v1/plants/profile")
    suspend fun getPlantProfile(
        @Query("plantId") plantId: Long
    ): Response<GetPlantProfile>

    @GET("/api/v1/diaries/by-month")
    suspend fun getPlantDiaries(
        @Query("plantId") plantId: Long
    ): Response<GetPlantDiaries>

    @Multipart
    @POST("/api/v1/diaries")
    suspend fun saveNewDiary(
        @Part("request") request: RequestBody,
        @Part plantImage: MultipartBody.Part
    ): Response<SaveNewDiary>

    @Multipart
    @PUT("/api/v1/diaries")
    suspend fun updateDiary(
        @Query("diaryId") diaryId: Long,
        @Part("request") request: RequestBody,
        @Part plantImage: MultipartBody.Part?
    ): Response<Unit>

    @DELETE("/api/v1/diaries")
    suspend fun deleteDiary(
        @Query("diaryId") diaryId: Long
    ): Response<Unit>
}