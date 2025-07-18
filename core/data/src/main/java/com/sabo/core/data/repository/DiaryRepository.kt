package com.sabo.core.data.repository

import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.data.Result

interface DiaryRepository {
    suspend fun saveNewPlant(name: String, category: String, shine: Int, place: PlantEnvironmentPlace): Result<Unit>

    suspend fun getPlantCategories(keyword: String): Result<List<String>>
}