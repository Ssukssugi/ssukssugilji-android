package com.sabo.feature.diary.plantadd.categorySearch

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel(assistedFactory = PlantCategorySearchViewModel.Factory::class)
class PlantCategorySearchViewModel @AssistedInject constructor(
    private val diaryRepository: DiaryRepository,
    @Assisted val searchKeyword: String
) : ViewModel() {

    private val searchState = TextFieldState(
        initialText = searchKeyword,
        initialSelection = TextRange(searchKeyword.length)
    )

    private val _state = MutableStateFlow(
        PlantSearchState(
            textFieldState = searchState
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            snapshotFlow { searchState.text }
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .debounce(500L)
                .collect { onSearchedCategory(it.toString()) }
        }
    }

    private suspend fun onSearchedCategory(keyword: String) {
        diaryRepository.getPlantCategories(keyword).handle(
            onSuccess = { response ->
                _state.value = _state.value.copy(
                    searchResult = response
                )
            },
            onError = {
                _state.value = _state.value.copy(searchResult = emptyList())
            }
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            searchKeyword: String
        ): PlantCategorySearchViewModel
    }
}