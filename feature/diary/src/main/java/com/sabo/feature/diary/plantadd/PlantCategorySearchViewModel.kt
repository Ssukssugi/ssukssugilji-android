package com.sabo.feature.diary.plantadd

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = PlantCategorySearchViewModel.Factory::class)
class PlantCategorySearchViewModel @AssistedInject constructor(
    @Assisted val searchKeyword: String
): ViewModel() {


    @AssistedFactory
    interface Factory {
        fun create(
            searchKeyword: String
        ): PlantCategorySearchViewModel
    }

}