package com.sabo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.data.Result
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.data.repository.ProfileRepository
import com.sabo.core.mapper.DateMapper.toLocalDate
import com.sabo.core.model.NetworkErrorEvent
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.core.toolkit.NetworkErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
    private val profileRepository: ProfileRepository,
    private val networkErrorManager: NetworkErrorManager
) : ContainerHost<HomeUiState, HomeEvent>, ViewModel() {

    override val container: Container<HomeUiState, HomeEvent> = container(
        initialState = HomeUiState(
            plantList = emptyList(),
            plantContent = PlantContent.Loading
        ),
        onCreate = {
            initDataLoad()
            viewModelScope.launch {
                launch {
                    subIntent {
                        selectedPlantId.collect { plantId ->
                            plantId?.let { fetchPlantContent(it) }
                        }
                    }
                }
                launch {
                    subIntent {
                        networkErrorManager.showScreen.collect {
                            reduce {
                                state.copy(
                                    plantContent = PlantContent.NetworkError(it),
                                    plantList = listOf(PlantListItem.AddPlant)
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    private val _selectedPlantId = MutableStateFlow<Long?>(null)
    val selectedPlantId: StateFlow<Long?> = _selectedPlantId.asStateFlow()

    val showNetworkErrorDialog = networkErrorManager.showDialog

    fun initDataLoad() = intent {
        viewModelScope.launch {
            val profileDeferred = async { profileRepository.getUserProfile() }
            val storyDeferred = async { diaryRepository.getMyPlants(false) }

            val profileResult = profileDeferred.await()
            val storyResult = storyDeferred.await()

            when {
                profileResult is Result.Error -> {
                    if (profileResult.isNetworkError) {
                        networkErrorManager.sendDialogEvent(NetworkErrorEvent.NoInternet)
                    }
                }

                storyResult is Result.Error -> {
                    if (storyResult.isNetworkError) {
                        networkErrorManager.sendDialogEvent(NetworkErrorEvent.NoInternet)
                    }
                    reduce { state.copy(plantList = listOf(PlantListItem.AddPlant)) }
                }

                storyResult is Result.Success -> {
                    _selectedPlantId.update { storyResult.data.firstOrNull()?.plantId }

                    reduce {
                        state.copy(
                            plantList = listOf(PlantListItem.AddPlant) + storyResult.data.map { plant ->
                                PlantListItem.Plant(
                                    id = plant.plantId,
                                    name = plant.name,
                                    image = plant.image,
                                    isSelected = plant.plantId == _selectedPlantId.value
                                )
                            },
                            plantContent = if (storyResult.data.isEmpty()) PlantContent.Empty else PlantContent.Loading
                        )
                    }
                }
            }
        }
    }

    private fun fetchPlantStory() = intent {
        val plants: List<PlantListItem> = when (val response = diaryRepository.getMyPlants(false)) {
            is Result.Success -> {
                _selectedPlantId.update { response.data.firstOrNull()?.plantId }

                if (response.data.isEmpty()) {
                    reduce {
                        state.copy(
                            plantContent = PlantContent.Empty
                        )
                    }
                }

                response.data.map { plant ->
                    PlantListItem.Plant(
                        id = plant.plantId,
                        name = plant.name,
                        image = plant.image,
                        isSelected = plant.plantId == _selectedPlantId.value
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
                plantList = plants
            )
        }
    }

    fun onSelectPlant(plantId: Long) = intent {
        _selectedPlantId.update { plantId }

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
                state.copy(plantContent = PlantContent.Loading)
            }
            val profileDeferred = async { diaryRepository.getPlantProfile(plantId) }
            val plantContentDeferred = async { diaryRepository.getPlantDiaries(plantId) }

            val profileResult = profileDeferred.await()
            val plantContentResult = plantContentDeferred.await()

            when {
                profileResult is Result.Error -> {
                    if (profileResult.isNetworkError) {
                        networkErrorManager.sendScreenEvent(NetworkErrorEvent.NoInternet)
                    }
                }
                plantContentResult is Result.Error -> {
                    if (plantContentResult.isNetworkError) {
                        networkErrorManager.sendScreenEvent(NetworkErrorEvent.NoInternet)
                    }
                }
                profileResult is Result.Success && plantContentResult is Result.Success -> {
                    reduce {
                        state.copy(
                            plantContent = PlantContent.PlantInfo(
                                id = plantId,
                                place = profileResult.data.place,
                                name = profileResult.data.name,
                                image = profileResult.data.plantImage,
                                category = profileResult.data.plantCategory,
                                shine = profileResult.data.shine,
                                historyList = plantContentResult.data.byMonth.map { content ->
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
                    }
                }
            }
        }
    }

    fun onClickDiaryDetail(id: Long) = intent {
        val plantId = _selectedPlantId.value ?: return@intent
        postSideEffect(HomeEvent.NavigateToDiaryDetail(plantId = plantId, diaryId = id))
    }

    fun onClickMore(plantId: Long) = intent {
        val plant = state.plantList.filterIsInstance<PlantListItem.Plant>()
            .find { it.id == plantId } ?: return@intent

        postSideEffect(HomeEvent.ShowPlantOptions(plant))
    }

    fun onEditPlant() = intent {
        val plant = state.plantContent as? PlantContent.PlantInfo ?: return@intent
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
        val plant = state.plantContent as? PlantContent.PlantInfo ?: return@intent
        reduce {
            state.copy(plantContent = PlantContent.Loading)
        }
        diaryRepository.deletePlant(plantId = plant.id).handle(
            onSuccess = {
                fetchPlantStory()
                postSideEffect(HomeEvent.ShowSnackBarDeletePlant)
            }
        )
    }
}