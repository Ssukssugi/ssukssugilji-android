package com.sabo.feature.diary.write

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.model.CareType
import com.sabo.core.navigator.DiaryWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    imageDateLoader: ImageDateLoader,
    private val diaryRepository: DiaryRepository
) : ContainerHost<DiaryWriteUiState, DiaryWriteSideEffect>, ViewModel() {

    private val route = savedStateHandle.toRoute<DiaryWrite>()
    override val container: Container<DiaryWriteUiState, DiaryWriteSideEffect> = container(
        initialState = DiaryWriteUiState(isLoading = true, imageUri = route.imageUri.toUri(), date = imageDateLoader.getImageDateFromUri(route.imageUri.toUri())),
        onCreate = { loadPlants() }
    )

    val isSaveEnabled = container.stateFlow
        .map { it.plants.any { plant -> plant.isSelected } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private fun loadPlants() = intent {
        diaryRepository.getMyPlants().handle(
            onSuccess = {
                reduce {
                    state.copy(
                        isLoading = false,
                        plants = it.map { plant -> PlantListItem(id = plant.plantId, name = plant.name, imageUrl = plant.image) }
                    )
                }
            }
        )
    }

    fun onClickPlant(plantId: Long) = intent {
        reduce {
            state.copy(
                plants = state.plants.map { plant ->
                    plant.copy(isSelected = plant.id == plantId)
                }
            )
        }
    }

    fun onChangeDate(millis: Long) = intent {
        reduce {
            state.copy(
                date = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDate()
            )
        }
    }

    fun onClickCareType(careType: CareType) = intent {
        reduce {
            state.copy(
                careTypes = state.careTypes.map {
                    if (it.type == careType) {
                        it.copy(isSelected = !it.isSelected)
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun onClickSave() = intent {
        if (isSaveEnabled.value.not()) {
            postSideEffect(DiaryWriteSideEffect.ShowSnackBar(DiaryWriteSideEffect.ShowSnackBar.SnackBarType.PLANT_REQUIRED))
            return@intent
        }
        reduce { state.copy(isSaveLoading = true) }
        diaryRepository.savePlantDiary(
            plantId = state.plants.first { it.isSelected }.id,
            imageUrl = state.imageUri.toString(),
            careTypes = state.careTypes.filter { it.isSelected }.map { it.type },
            date = state.date,
            diary = state.content.text.toString()
        ).handle(
            onSuccess = {
                reduce {
                    state.copy(isSaveSuccess = true)
                }
            },
            onError = {

            },
            onFinish = {
                reduce {
                    state.copy(isSaveLoading = false)
                }
            }
        )
    }
    
    fun onClickGoToDiaryDetail() = intent {
        val plantId = state.plants.first { it.isSelected }.id
        postSideEffect(DiaryWriteSideEffect.NavigateToDetail(plantId = plantId))
    }
}