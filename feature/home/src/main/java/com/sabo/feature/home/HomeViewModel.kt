package com.sabo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.data.Result
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.data.repository.TownRepository
import com.sabo.core.mapper.DateMapper.toLocalDate
import com.sabo.core.navigator.model.PlantAddEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val townRepository: TownRepository
) : ContainerHost<HomeUiState, HomeEvent>, ViewModel() {

    override val container: Container<HomeUiState, HomeEvent> = container(
        initialState = HomeUiState(
            isLoading = true,
            plantList = emptyList(),
            homeContent = HomeContent.Diary(plantContent = PlantContent.Loading)
        ),
        onCreate = {
            fetchPlantStory()
            viewModelScope.launch {
                selectedPlantId.collect {
                    if (it == null) {
                        fetchTownGrowth()
                    } else {
                        fetchPlantContent(it)
                    }
                }
            }
        }
    )

    private val selectedPlantId = MutableStateFlow<Long?>(null)

    private fun fetchPlantStory() = intent {
        val plants: List<PlantListItem> = when (val response = diaryRepository.getMyPlants()) {
            is Result.Success -> {
                response.data.map { plant ->
                    PlantListItem.Plant(
                        id = plant.plantId,
                        name = plant.name,
                        image = plant.image,
                        isSelected = plant.plantId == selectedPlantId.value
                    ) as PlantListItem
                }
            }

            is Result.Error -> {
                emptyList()
            }
        }.toMutableList().apply {
            add(0, PlantListItem.AddPlant)
        }

        reduce {
            state.copy(
                isLoading = false,
                plantList = plants,
                homeContent = HomeContent.Diary(
                    plantContent = if (plants.size == 1) PlantContent.Empty else PlantContent.Loading
                )
            )
        }
    }

    fun onSelectPlant(plantId: Long) = intent {
        selectedPlantId.update { plantId }

        val updatedPlantList = state.plantList.map {
            when (it) {
                is PlantListItem.Plant -> it.copy(isSelected = it.id == plantId)
                is PlantListItem.AddPlant -> it
            }
        }

        reduce {
            state.copy(plantList = updatedPlantList)
        }
    }

    private fun fetchPlantContent(plantId: Long) = intent {
        viewModelScope.launch {
            reduce {
                state.copy(
                    homeContent = HomeContent.Diary(plantContent = PlantContent.Loading)
                )
            }
            val profileDeferred = async { diaryRepository.getPlantProfile(plantId) }
            val plantContentDeferred = async { diaryRepository.getPlantDiaries(plantId) }

            val profile = (profileDeferred.await() as? Result.Success)?.data
            val plantContent = (plantContentDeferred.await() as? Result.Success)?.data

            if (profile != null && plantContent != null) {
                reduce {
                    state.copy(
                        homeContent = HomeContent.Diary(
                            plantContent = PlantContent.PlantInfo(
                                id = plantId,
                                place = profile.place,
                                name = profile.name,
                                image = profile.plantImage,
                                category = profile.plantCategory,
                                shine = profile.shine,
                                historyList = plantContent.byMonth.map { content ->
                                    PlantHistory(
                                        month = content.month,
                                        year = content.year,
                                        diaryList = content.diaries.map { diary ->
                                            Diary(
                                                id = diary.diaryId,
                                                date = diary.date.toLocalDate(),
                                                image = diary.image,
                                                content = diary.content,
                                                cares = diary.cares.map { care ->
                                                    CareType.valueOf(care.name)
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        )
                    )
                }
            }
        }
    }

    fun onClickDiaryDetail(id: Long) = intent {
        val plantId = selectedPlantId.value ?: return@intent
        postSideEffect(HomeEvent.NavigateToDiaryDetail(plantId = plantId, diaryId = id))
    }

    fun onClickMore(plantId: Long) = intent {
        val plant = state.plantList.filterIsInstance<PlantListItem.Plant>()
            .find { it.id == plantId } ?: return@intent

        postSideEffect(HomeEvent.ShowPlantOptions(plant))
    }

    fun onEditPlant() = intent {
        val diaryContent = state.homeContent as? HomeContent.Diary ?: return@intent
        val plant = diaryContent.plantContent as? PlantContent.PlantInfo ?: return@intent
        postSideEffect(
            HomeEvent.NavigateToPlantEdit(
                PlantAddEdit.PlantEdit(
                    plantId = plant.id,
                    name = plant.name,
                    category = plant.category,
                    shine = plant.shine,
                    place = plant.place
                )
            )
        )
    }

    fun refreshPlantContent() {
        fetchPlantContent(selectedPlantId.value ?: return)
    }

    fun onDeletePlant() = intent {
        val diaryContent = state.homeContent as? HomeContent.Diary ?: return@intent
        val plant = diaryContent.plantContent as? PlantContent.PlantInfo ?: return@intent
        reduce {
            state.copy(
                homeContent = HomeContent.Diary(plantContent = PlantContent.Loading)
            )
        }
        diaryRepository.deletePlant(plantId = plant.id).handle(
            onSuccess = {
                fetchPlantStory()
                postSideEffect(HomeEvent.ShowSnackBarDeletePlant)
            }
        )
    }

    private fun fetchTownGrowth() = intent {
        reduce {
            state.copy(
                homeContent = HomeContent.Town(
                    townContent = TownContent(isLoading = true, dataList = emptyList())
                )
            )
        }

        when (val result = townRepository.getTownGrowth(null)) {
            is Result.Success -> {
                val townItems = result.data.contents.map { growth ->
                    val beforeDate = LocalDate.parse(growth.before.date)
                    val afterDate = LocalDate.parse(growth.after.date)
                    val dateDiff = ChronoUnit.DAYS.between(beforeDate, afterDate).toInt()

                    TownListItem.Post(
                        id = growth.growthId,
                        profile = growth.plant.image,
                        plantName = growth.plant.name,
                        nickName = growth.owner.nickname,
                        oldImage = growth.before.imageUrl,
                        newImage = growth.after.imageUrl,
                        dateDiff = dateDiff
                    )
                }

                reduce {
                    state.copy(
                        homeContent = HomeContent.Town(
                            townContent = TownContent(
                                isLoading = false,
                                dataList = townItems
                            )
                        )
                    )
                }
            }

            is Result.Error -> {
                reduce {
                    state.copy(
                        homeContent = HomeContent.Town(
                            townContent = TownContent(isLoading = false, dataList = emptyList())
                        )
                    )
                }
            }
        }
    }

    fun onSelectTown() = intent {
        selectedPlantId.update { null }
        reduce {
            state.copy(
                plantList = state.plantList.map { plant ->
                    when (plant) {
                        PlantListItem.AddPlant -> plant
                        is PlantListItem.Plant -> plant.copy(isSelected = false)
                    }
                }
            )
        }
    }
}