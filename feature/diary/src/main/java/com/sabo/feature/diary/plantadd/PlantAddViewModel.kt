package com.sabo.feature.diary.plantadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.feature.diary.plantadd.model.PlantAddState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlantAddViewModel @Inject constructor(

): ViewModel() {

    private val _state = MutableStateFlow<PlantAddState>(PlantAddState.Input())
    val state = _state.asStateFlow()


}