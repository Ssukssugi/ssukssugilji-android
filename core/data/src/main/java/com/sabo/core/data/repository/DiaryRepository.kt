package com.sabo.core.data.repository

import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.data.Result
import com.sabo.core.network.model.response.GetMyPlant
import com.sabo.core.network.model.response.GetPlantDiaries
import com.sabo.core.network.model.response.GetPlantProfile

interface DiaryRepository {
    suspend fun saveNewPlant(name: String, category: String, shine: Int, place: PlantEnvironmentPlace): Result<Unit>

    suspend fun getPlantCategories(keyword: String): Result<List<String>>

    suspend fun getMyPlants(): Result<List<GetMyPlant.Plant>>

    suspend fun getPlantProfile(plantId: Long): Result<GetPlantProfile>

    suspend fun getPlantDiaries(plantId: Long): Result<GetPlantDiaries>
}