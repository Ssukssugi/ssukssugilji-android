package com.sabo.feature.diary.plantadd.categorySearch

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.domain.handle
import com.sabo.core.domain.repository.DiaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel(assistedFactory = PlantCategorySearchViewModel.Factory::class)
class PlantCategorySearchViewModel @AssistedInject constructor(
    private val diaryRepository: DiaryRepository,
    @Assisted val searchKeyword: String
) : ViewModel() {

    private val _state = MutableStateFlow(
        PlantSearchState(
            textFieldState = TextFieldState(
                initialText = searchKeyword,
                initialSelection = TextRange(searchKeyword.length)
            )
        )
    )
    val state = _state.asStateFlow()


    private val keywordFlow = state
        .map { it.textFieldState.text }
        .debounce(500L)
        .distinctUntilChanged()

    init {
        keywordFlow.onEach {
            onSearchedCategory(it.toString())
        }.launchIn(viewModelScope)
    }

    private fun onSearchedCategory(keyword: String) {
        viewModelScope.launch {
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
    }

    @AssistedFactory
    interface Factory {
        fun create(
            searchKeyword: String
        ): PlantCategorySearchViewModel
    }
}