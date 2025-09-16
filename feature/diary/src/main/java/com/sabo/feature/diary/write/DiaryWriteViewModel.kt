package com.sabo.feature.diary.write

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sabo.core.navigator.RouteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ContainerHost<DiaryWriteUiState, DiaryWriteSideEffect>, ViewModel() {

    private val route = savedStateHandle.toRoute<RouteModel.DiaryWrite>()
    override val container: Container<DiaryWriteUiState, DiaryWriteSideEffect> = container(
        initialState = DiaryWriteUiState(isLoading = true, imageUri = route.imageUri.toUri())
    )
}