package com.sabo.core.data.repository

import androidx.core.net.toUri
import com.sabo.core.data.Result
import com.sabo.core.data.handleResult
import com.sabo.core.model.CareType
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.network.model.request.SaveNewDiaryRequest
import com.sabo.core.network.model.request.SaveNewPlantRequest
import com.sabo.core.network.model.response.GetMyPlant
import com.sabo.core.network.model.response.GetPlantDiaries
import com.sabo.core.network.model.response.GetPlantProfile
import com.sabo.core.network.service.DiaryService
import com.sabo.core.network.util.MultipartUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DefaultDiaryRepository @Inject constructor(
    private val diaryService: DiaryService,
    private val multipartUtil: MultipartUtil
): DiaryRepository {
    override suspend fun saveNewPlant(
        name: String,
        category: String,
        shine: Int,
        place: PlantEnvironmentPlace
    ): Result<Unit> = handleResult(
        execute = {
            val request = SaveNewPlantRequest(
                name = name,
                plantCategory = category,
                plantEnvironment = SaveNewPlantRequest.PlantEnvironment(
                    shine = shine,
                    place = when (place) {
                        PlantEnvironmentPlace.VERANDAH -> SaveNewPlantRequest.PlantEnvironment.Place.VERANDAH
                        PlantEnvironmentPlace.WINDOW -> SaveNewPlantRequest.PlantEnvironment.Place.WINDOW
                        PlantEnvironmentPlace.ROOM -> SaveNewPlantRequest.PlantEnvironment.Place.ROOM
                        PlantEnvironmentPlace.LIVINGROOM -> SaveNewPlantRequest.PlantEnvironment.Place.LIVINGROOM
                        PlantEnvironmentPlace.HALLWAY -> SaveNewPlantRequest.PlantEnvironment.Place.HALLWAY
                        PlantEnvironmentPlace.ETC -> SaveNewPlantRequest.PlantEnvironment.Place.ETC
                    }
                )
            )
            diaryService.saveNewPlant(request)
        },
        transform = {}
    )

    override suspend fun getPlantCategories(keyword: String): Result<List<String>> = handleResult(
        execute = {
            diaryService.getPlantCategories(keyword)
        },
        transform = { it.map { model ->  model.name } }
    )

    override suspend fun getMyPlants(): Result<List<GetMyPlant.Plant>> = handleResult(
        execute = {
            diaryService.getMyPlants()
        },
        transform = { it.plants }
    )

    override suspend fun getPlantProfile(plantId: Long): Result<GetPlantProfile> = handleResult(
        execute = {
            diaryService.getPlantProfile(plantId)
        },
        transform = { it }
    )

    override suspend fun getPlantDiaries(plantId: Long): Result<GetPlantDiaries> = handleResult(
        execute = { diaryService.getPlantDiaries(plantId) },
        transform = { it }
    )

    override suspend fun savePlantDiary(plantId: Long, date: LocalDate, careTypes: List<CareType>, diary: String, imageUrl: String) = handleResult(
        execute = {
            val request = SaveNewDiaryRequest(
                plantId = plantId,
                date = date.format(DateTimeFormatter.ISO_DATE),
                careTypes = careTypes.map { it.name },
                diary = diary
            )

            val requestBody = multipartUtil.createDiaryRequestBody(request)
            val imagePart = multipartUtil.createImageMultipartBody(imageUri = imageUrl.toUri())
                ?: throw IllegalArgumentException("Image part cannot be null")
            diaryService.saveNewDiary(requestBody, imagePart)
        },
        transform = {
            it
        }
    )
}