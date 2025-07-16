package com.sabo.core.domain.repository

import com.sabo.core.domain.Result
import com.sabo.core.domain.model.PlantEnvironmentPlace

interface DiaryRepository {
    suspend fun saveNewPlant(name: String, category: String, shine: Int, place: PlantEnvironmentPlace): Result<Unit>

    suspend fun getPlantCategories(keyword: String): Result<List<String>>
}