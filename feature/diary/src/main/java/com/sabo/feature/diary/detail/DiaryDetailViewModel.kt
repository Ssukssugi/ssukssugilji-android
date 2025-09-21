package com.sabo.feature.diary.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sabo.core.data.Result
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.designsystem.component.CareTypeIcon
import com.sabo.core.navigator.RouteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryRepository: DiaryRepository
): ContainerHost<DiaryDetailUiState, DiaryDetailUiEvent>, ViewModel() {
    private val plantId = savedStateHandle.toRoute<RouteModel.DiaryDetail>().plantId

    override val container: Container<DiaryDetailUiState, DiaryDetailUiEvent> = container(
        initialState = DiaryDetailUiState(isLoading = true),
        onCreate = { loadDiaries() }
    )

    fun onSelectDiary(index: Int) = intent {
        reduce {
        state.copy(selectedDiaryIndex = index)
        }
        updateSelectedDiaryContent()
    }

    private fun loadDiaries() = intent {
        viewModelScope.launch {
            reduce { state.copy(isLoading = true) }
            val plantProfileDeferred = async { diaryRepository.getPlantProfile(plantId) }
            val plantDiariesDeferred = async { diaryRepository.getPlantDiaries(plantId) }

            val plantProfile = (plantProfileDeferred.await() as? Result.Success)?.data ?: return@launch
            val plantDiariesResult = plantDiariesDeferred.await()

            if (plantDiariesResult is Result.Success) {
                val allDiaries = plantDiariesResult.data.byMonth.flatMap { it.diaries }

                reduce {
                    state.copy(
                        isLoading = false,
                        profileImage = plantProfile.plantImage ?: "",
                        nickname = plantProfile.name,
                        diaries = allDiaries,
                        selectedDiaryIndex = if (allDiaries.isNotEmpty()) 0 else -1
                    )
                }

                if (allDiaries.isNotEmpty()) {
                    updateSelectedDiaryContent()
                }
            } else {
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun updateSelectedDiaryContent() = intent {
        val selectedDiary = state.diaries.getOrNull(state.selectedDiaryIndex) ?: return@intent

        val careTypes = selectedDiary.cares.mapNotNull { careType ->
            CareTypeIcon.entries.find { it.type == careType }
        }

        reduce {
            state.copy(
                content = Content.Success(
                    careTypes = careTypes,
                    updatedAt = selectedDiary.date,
                    diary = selectedDiary.content,
                    image = selectedDiary.image
                )
            )
        }
    }
}