package com.sabo.feature.diary.plantadd

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.navigator.model.PlantAddEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryRepository: DiaryRepository
): ViewModel() {

    private val route: PlantAddEdit = runCatching {
        savedStateHandle.toRoute<PlantAddEdit.PlantEdit>()
    }.getOrElse {
        savedStateHandle.toRoute<PlantAddEdit.PlantAdd>()
    }

    private val _state = MutableStateFlow<PlantAddState>(
        when (route) {
            is PlantAddEdit.PlantAdd -> PlantAddState.Input(
                mode = PlantAddState.Input.Mode.Add
            )
            is PlantAddEdit.PlantEdit -> PlantAddState.Input(
                mode = PlantAddState.Input.Mode.Edit(plantId = route.plantId),
                textFieldState = TextFieldState(route.name),
                plantCategory = route.category,
                lightAmount = route.shine?.let { LightAmount.fromValue(it - 1) } ?: LightAmount.NOT_SET,
                place = when (route.place) {
                    PlantEnvironmentPlace.VERANDAH -> PlantPlace.VERANDA
                    PlantEnvironmentPlace.WINDOW -> PlantPlace.WINDOW
                    PlantEnvironmentPlace.LIVINGROOM -> PlantPlace.LIVINGROOM
                    PlantEnvironmentPlace.HALLWAY -> PlantPlace.HALLWAY
                    PlantEnvironmentPlace.ROOM -> PlantPlace.ROOM
                    PlantEnvironmentPlace.ETC -> PlantPlace.OTHER
                    null -> null
                }
            )
        }
    )
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<PlantAddSideEffect>()
    val event = _event.asSharedFlow()

    private val textFieldFlow = snapshotFlow {
        (state.value as? PlantAddState.Input)?.textFieldState?.text?.toString() ?: ""
    }

    val isAddable = combine(_state, textFieldFlow) { state, textFieldText ->
        val inputState = state as? PlantAddState.Input
        when (route) {
            PlantAddEdit.PlantAdd -> {
                val isTextNotEmpty = textFieldText.isNotEmpty()
                val isCategorySelected = inputState?.plantCategory != null
                isTextNotEmpty && isCategorySelected
            }
            is PlantAddEdit.PlantEdit -> {
                val isSameName = textFieldText == route.name
                val isSameCategory = inputState?.plantCategory == route.category
                val isSameLightAmount = inputState?.lightAmount == (route.shine?.let { LightAmount.fromValue(it - 1) } ?: LightAmount.NOT_SET)
                val isSamePlace = inputState?.place == when (route.place) {
                    PlantEnvironmentPlace.VERANDAH -> PlantPlace.VERANDA
                    PlantEnvironmentPlace.WINDOW -> PlantPlace.WINDOW
                    PlantEnvironmentPlace.LIVINGROOM -> PlantPlace.LIVINGROOM
                    PlantEnvironmentPlace.HALLWAY -> PlantPlace.HALLWAY
                    PlantEnvironmentPlace.ROOM -> PlantPlace.ROOM
                    PlantEnvironmentPlace.ETC -> PlantPlace.OTHER
                    null -> null
                }
                (isSameName && isSameCategory && isSameLightAmount && isSamePlace).not() && textFieldText.isNotEmpty()
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun onSelectedPlantCategory(category: String) {
        val state = state.value as? PlantAddState.Input ?: return
        _state.value = state.copy(plantCategory = category)
    }

    fun onClickSave() {
        val inputState = state.value as? PlantAddState.Input ?: return
        when (val mode = inputState.mode) {
            PlantAddState.Input.Mode.Add -> savePlant(inputState)
            is PlantAddState.Input.Mode.Edit -> updatePlant(mode.plantId, inputState)
        }
    }

    private fun savePlant(inputState: PlantAddState.Input) {
        viewModelScope.launch {
            diaryRepository.saveNewPlant(
                name = inputState.textFieldState.text.toString(),
                category = inputState.plantCategory ?: "",
                shine = inputState.lightAmount.takeIf { it != LightAmount.NOT_SET }?.let { it.value + 1 },
                place = when(inputState.place) {
                    PlantPlace.VERANDA -> PlantEnvironmentPlace.VERANDAH
                    PlantPlace.WINDOW -> PlantEnvironmentPlace.WINDOW
                    PlantPlace.LIVINGROOM -> PlantEnvironmentPlace.LIVINGROOM
                    PlantPlace.HALLWAY -> PlantEnvironmentPlace.HALLWAY
                    PlantPlace.ROOM -> PlantEnvironmentPlace.ROOM
                    PlantPlace.OTHER -> PlantEnvironmentPlace.ETC
                    null -> null
                }
            ).handle(
                onSuccess = {
                    _state.value = PlantAddState.SaveSuccess(it.plantId)
                },
                onError = {

                }
            )
        }
    }
    
    private fun updatePlant(id: Long, inputState: PlantAddState.Input) {
        viewModelScope.launch {
            if (inputState.place == null) return@launch
            diaryRepository.updatePlant(
                plantId = id,
                name = inputState.textFieldState.text.toString(),
                category = inputState.plantCategory ?: "",
                shine = inputState.lightAmount.value + 1,
                place = when (inputState.place) {
                    PlantPlace.VERANDA -> PlantEnvironmentPlace.VERANDAH
                    PlantPlace.WINDOW -> PlantEnvironmentPlace.WINDOW
                    PlantPlace.LIVINGROOM -> PlantEnvironmentPlace.LIVINGROOM
                    PlantPlace.HALLWAY -> PlantEnvironmentPlace.HALLWAY
                    PlantPlace.ROOM -> PlantEnvironmentPlace.ROOM
                    PlantPlace.OTHER -> PlantEnvironmentPlace.ETC
                }
            ).handle(
                onSuccess = {
                    _event.emit(PlantAddSideEffect.UpdateSuccess)
                }
            )
        }
    }

    fun onClickLightStep(step: Int) {
        val state = state.value as? PlantAddState.Input ?: return
        _state.value = state.copy(lightAmount = LightAmount.fromValue(step))
    }

    fun onClickPlace(place: PlantPlace) {
        val state = state.value as? PlantAddState.Input ?: return
        _state.value = state.copy(place = place)
    }
}