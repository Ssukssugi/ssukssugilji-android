package com.sabo.feature.diary.plantadd

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.domain.handle
import com.sabo.core.domain.model.PlantEnvironmentPlace
import com.sabo.core.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantAddViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
): ViewModel() {

    private val _state = MutableStateFlow<PlantAddState>(PlantAddState.Input())
    val state = _state.asStateFlow()

    val isAddable = combine(
        snapshotFlow { (state.value as? PlantAddState.Input)?.textFieldState?.text?.isNotEmpty() ?: false },
        snapshotFlow { (state.value as? PlantAddState.Input)?.plantCategory != null }
    ) { isTextNotEmpty, isCategorySelected ->
        isTextNotEmpty && isCategorySelected
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun savePlant() {
        viewModelScope.launch {
            val inputState = state.value as? PlantAddState.Input ?: return@launch
            if (inputState.place == null) return@launch
            diaryRepository.saveNewPlant(
                name = inputState.textFieldState.text.toString(),
                category = inputState.plantCategory?.name ?: "",
                shine = inputState.lightAmount.value + 1,
                place = when(inputState.place) {
                    PlantPlace.VERANDA -> PlantEnvironmentPlace.VERANDAH
                    PlantPlace.WINDOW -> PlantEnvironmentPlace.WINDOW
                    PlantPlace.LIVINGROOM -> PlantEnvironmentPlace.LIVINGROOM
                    PlantPlace.HALLWAY -> PlantEnvironmentPlace.HALLWAY
                    PlantPlace.ROOM -> PlantEnvironmentPlace.ROOM
                    PlantPlace.OTHER -> PlantEnvironmentPlace.ETC
                }
            ).handle(
                onSuccess = {
                    _state.value = PlantAddState.SaveSuccess
                },
                onError = {

                }
            )
        }
    }
}