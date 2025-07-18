package com.sabo.core.data.repository

import com.sabo.core.data.handleResult
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.network.model.request.SaveNewPlantRequest
import com.sabo.core.network.service.DiaryService
import javax.inject.Inject
import com.sabo.core.data.Result

class DefaultDiaryRepository @Inject constructor(
    private val diaryService: DiaryService
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
        transform = { it }
    )
}