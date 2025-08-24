package com.sabo.feature.diary.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(

): ContainerHost<DiaryDetailUiState, DiaryDetailUiEvent>, ViewModel() {
    override val container: Container<DiaryDetailUiState, DiaryDetailUiEvent> = container(
        initialState = DiaryDetailUiState(isLoading = true)
    )
}