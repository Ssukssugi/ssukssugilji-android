package com.sabo.core.data.repository

import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.data.Result
import com.sabo.core.model.CareType
import com.sabo.core.network.model.response.GetMyPlant
import com.sabo.core.network.model.response.GetPlantCategories
import com.sabo.core.network.model.response.GetPlantDiaries
import com.sabo.core.network.model.response.GetPlantProfile
import com.sabo.core.network.model.response.SaveNewDiary
import com.sabo.core.network.model.response.SaveNewPlant
import java.time.LocalDate

interface DiaryRepository {
    suspend fun saveNewPlant(name: String, category: String, shine: Int?, place: PlantEnvironmentPlace?): Result<SaveNewPlant>

    suspend fun updatePlant(plantId: Long, name: String, category: String, shine: Int, place: PlantEnvironmentPlace): Result<Unit>

    suspend fun deletePlant(plantId: Long): Result<Unit>

    suspend fun getPlantCategories(keyword: String): Result<List<GetPlantCategories>>

    suspend fun getMyPlants(includeDiaryCount: Boolean): Result<List<GetMyPlant.Plant>>

    suspend fun getPlantProfile(plantId: Long): Result<GetPlantProfile>

    suspend fun getPlantDiaries(plantId: Long): Result<GetPlantDiaries>

    suspend fun savePlantDiary(plantId: Long, date: LocalDate, careTypes: List<CareType>, diary: String, imageUrl: String): Result<SaveNewDiary>

    suspend fun updateDiaryDetail(diaryId: Long, plantId: Long, date: LocalDate, careTypes: List<CareType>, diary: String, imageUrl: String, updateImage: Boolean): Result<Unit>

    suspend fun deleteDiary(diaryId: Long): Result<Unit>
}