package com.sabo.core.data.repository

import androidx.core.net.toUri
import com.sabo.core.data.Result
import com.sabo.core.data.handleResult
import com.sabo.core.model.CareType
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.network.model.request.SaveNewDiaryRequest
import com.sabo.core.network.model.request.SaveNewPlantRequest
import com.sabo.core.network.model.request.UpdateDiaryRequest
import com.sabo.core.network.model.response.GetMyPlant
import com.sabo.core.network.model.response.GetPlantCategories
import com.sabo.core.network.model.response.GetPlantDiaries
import com.sabo.core.network.model.response.GetPlantProfile
import com.sabo.core.network.model.response.SaveNewPlant
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
        shine: Int?,
        place: PlantEnvironmentPlace?
    ): Result<SaveNewPlant> = handleResult(
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
                        null -> null
                    }
                )
            )
            diaryService.saveNewPlant(request)
        },
        transform = { it }
    )

    override suspend fun updatePlant(plantId: Long, name: String, category: String, shine: Int, place: PlantEnvironmentPlace): Result<Unit> = handleResult(
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
            diaryService.updatePlant(plantId, request)
        },
        transform = {}
    )

    override suspend fun deletePlant(plantId: Long): Result<Unit> = handleResult(
        execute = {
            diaryService.deletePlant(plantId)
        },
        transform = {}
    )

    override suspend fun getPlantCategories(keyword: String): Result<List<GetPlantCategories>> = handleResult(
        execute = {
            diaryService.getPlantCategories(keyword)
        },
        transform = { it }
    )

    override suspend fun getMyPlants(includeDiaryCount: Boolean): Result<List<GetMyPlant.Plant>> = handleResult(
        execute = {
            diaryService.getMyPlants(diaryCount = includeDiaryCount)
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
            val imagePart = multipartUtil.createImageMultipartBody(imageUri = imageUrl.toUri()) ?: throw IllegalArgumentException("Image part cannot be null")
            diaryService.saveNewDiary(requestBody, imagePart)
        },
        transform = {
            it
        }
    )

    override suspend fun updateDiaryDetail(
        diaryId: Long,
        plantId: Long,
        date: LocalDate,
        careTypes: List<CareType>,
        diary: String,
        imageUrl: String,
        updateImage: Boolean
    ) = handleResult(
        execute = {
            val request = UpdateDiaryRequest(
                plantId = plantId,
                date = date.format(DateTimeFormatter.ISO_DATE),
                careTypes = careTypes.map { it.name },
                diary = diary,
                updateImage = updateImage
            )

            val requestBody = multipartUtil.createUpdateDiaryRequestBody(request)
            val imagePart = if (updateImage) {
                multipartUtil.createImageMultipartBody(imageUri = imageUrl.toUri())
            } else {
                null
            }
            diaryService.updateDiary(diaryId, requestBody, imagePart)
        },
        transform = {}
    )

    override suspend fun deleteDiary(diaryId: Long): Result<Unit> = handleResult(
        execute = {
            diaryService.deleteDiary(diaryId)
        },
        transform = {}
    )
}