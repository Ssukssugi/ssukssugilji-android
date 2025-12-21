package com.sabo.feature.home

import androidx.annotation.DrawableRes
import com.sabo.core.designsystem.R
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.navigator.model.PlantAddEdit
import java.time.LocalDate

data class HomeUiState(
    val isLoading: Boolean = false,
    val plantList: List<PlantListItem>,
    val plantContent: PlantContent
)

sealed interface PlantListItem {
    data object AddPlant : PlantListItem
    data class Plant(
        val id: Long,
        val name: String,
        val image: String?,
        val isSelected: Boolean
    ) : PlantListItem
}

sealed interface PlantContent {
    data object Loading : PlantContent
    data object Empty : PlantContent
    data class PlantInfo(
        val id: Long,
        val place: PlantEnvironmentPlace?,
        val name: String,
        val category: String,
        val image: String?,
        val shine: Int?,
        val historyList: List<PlantHistory>
    ) : PlantContent
}

data class PlantHistory(
    val year: Int,
    val month: Int,
    val diaryList: List<Diary>
)

data class Diary(
    val id: Long,
    val date: LocalDate,
    val image: String?,
    val content: String,
    val cares: List<CareType>
)

enum class CareType(@DrawableRes val resId: Int) {
    WATER(R.drawable.img_care_water), DIVIDING(R.drawable.img_care_dividing), PRUNING(R.drawable.img_care_cut), NUTRIENT(R.drawable.img_care_medicine)
}

sealed interface HomeEvent {
    data class NavigateToDiaryDetail(val plantId: Long, val diaryId: Long) : HomeEvent
    data class ShowPlantOptions(val plant: PlantListItem.Plant) : HomeEvent
    data class NavigateToPlantEdit(val route: PlantAddEdit.PlantEdit): HomeEvent
    data object ShowSnackBarDeletePlant : HomeEvent
}
