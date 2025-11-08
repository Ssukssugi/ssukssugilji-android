package com.sabo.feature.home

import androidx.annotation.DrawableRes
import com.sabo.core.designsystem.R
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.core.network.model.response.GetTownGrowth
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class HomeUiState(
    val isLoading: Boolean = false,
    val plantList: List<PlantListItem>,
    val homeContent: HomeContent
)

sealed interface HomeContent {
    data class Diary(
        val plantContent: PlantContent
    ): HomeContent
    data class Town(
        val townContent: TownContent
    ): HomeContent
}

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
        val place: PlantEnvironmentPlace,
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

data class TownContent(
    val isLoading: Boolean,
    val isNewUser: Boolean = false,
    val dataList: List<TownListItem>
)

sealed interface TownListItem {
    data class Post(
        val id: Long,
        val profile: String,
        val plantName: String,
        val nickName: String,
        val oldImage: String,
        val newImage: String,
        val dateDiff: Int
    ): TownListItem

    data class LoadMore(val lastId: Long): TownListItem
}

fun GetTownGrowth.GrowthContent.toPresentation(): TownListItem.Post {
    val beforeDate = LocalDate.parse(before.date)
    val afterDate = LocalDate.parse(after.date)
    val dateDiff = ChronoUnit.DAYS.between(beforeDate, afterDate).toInt()

    return TownListItem.Post(
        id = growthId,
        profile = plant.plantImage,
        plantName = plant.name,
        nickName = owner.nickname,
        oldImage = before.imageUrl,
        newImage = after.imageUrl,
        dateDiff = dateDiff
    )
}

enum class CareType(@DrawableRes val resId: Int) {
    WATER(R.drawable.img_care_water), DIVIDING(R.drawable.img_care_dividing), PRUNING(R.drawable.img_care_cut), NUTRIENT(R.drawable.img_care_medicine)
}

sealed interface HomeEvent {
    data class NavigateToDiaryDetail(val plantId: Long, val diaryId: Long) : HomeEvent
    data class ShowPlantOptions(val plant: PlantListItem.Plant) : HomeEvent
    data class NavigateToPlantEdit(val route: PlantAddEdit.PlantEdit): HomeEvent
    data object ShowSnackBarDeletePlant : HomeEvent
}
