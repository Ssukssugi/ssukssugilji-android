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
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
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
            subIntent {
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
                val townItems = result.data.growths
                    .distinctBy { it.growthId }
                    .map { growth -> growth.toPresentation() }

                val newList = if (townItems.isNotEmpty()) {
                    townItems + TownListItem.LoadMore(lastId = townItems.last().id)
                } else {
                    townItems
                }

                reduce {
                    state.copy(
                        homeContent = HomeContent.Town(
                            townContent = TownContent(
                                isLoading = false,
                                dataList = newList
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

    fun loadMoreTownGrowth(lastId: Long) = intent {
        val townState = state.homeContent as? HomeContent.Town ?: return@intent
        when (val result = townRepository.getTownGrowth(lastId)) {
            is Result.Success -> {
                val existingIds = townState.townContent.dataList
                    .filterIsInstance<TownListItem.Post>()
                    .map { it.id }
                    .toSet()

                val newGrowths = result.data.growths
                    .distinctBy { it.growthId }
                    .filter { it.growthId !in existingIds }

                val newTownItems = townState.townContent.dataList.toMutableList().apply {
                    removeAt(lastIndex)
                    addAll(newGrowths.map { it.toPresentation() })
                    if (newGrowths.isNotEmpty()) add(TownListItem.LoadMore(lastId = newGrowths.last().growthId))
                }

                reduce {
                    state.copy(
                        homeContent = townState.copy(
                            townContent = townState.townContent.copy(dataList = newTownItems)
                        )
                    )
                }
            }

            is Result.Error -> {}
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