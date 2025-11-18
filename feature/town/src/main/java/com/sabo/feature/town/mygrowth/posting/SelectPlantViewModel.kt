package com.sabo.feature.town.mygrowth.posting

import androidx.lifecycle.ViewModel
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.navigator.model.GrowthVariation
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectPlantViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ContainerHost<SelectPlantState, SelectPlantSideEffect>, ViewModel() {
    override val container: Container<SelectPlantState, SelectPlantSideEffect> = container(
        initialState = SelectPlantState(isLoading = true, plantList = emptyList()),
        onCreate = { fetchPlantList() }
    )

    private fun fetchPlantList() = intent {
        reduce { state.copy(isLoading = true) }
        diaryRepository.getMyPlants(includeDiaryCount = true).handle(
            onSuccess = { result ->
                reduce {
                    state.copy(
                        plantList = result.map { plant ->
                            Plant(
                                id = plant.plantId,
                                nickname = plant.name,
                                category = plant.plantCategory,
                                image = plant.image ?: "",
                                isSelected = false,
                                enabled = plant.diaryCount?.takeIf { it >= 2 } != null
                            )
                        }
                    )
                }
            },
            onFinish = { reduce { state.copy(isLoading = false) } }
        )
    }

    fun selectPlant(id: Long) = intent {
        val selectedItem = state.plantList.find { it.id == id } ?: return@intent
        if (selectedItem.enabled.not()) return@intent
        reduce {
            state.copy(
                plantList = state.plantList.map { plant ->
                    if (plant.id == id) {
                        plant.copy(isSelected = plant.isSelected.not())
                    } else {
                        plant.copy(isSelected = false)
                    }
                }
            )
        }
    }

    fun onClickComplete() = intent {
        val selectedPlant = state.plantList.find { it.isSelected } ?: return@intent
        postSideEffect(SelectPlantSideEffect.NavigateToGrowthVariation(GrowthVariation(plantId = selectedPlant.id, plantName = selectedPlant.nickname)))
    }
}