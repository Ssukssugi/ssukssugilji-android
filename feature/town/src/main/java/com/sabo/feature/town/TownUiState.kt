package com.sabo.feature.town

import com.sabo.core.network.model.response.GetTownGrowth
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class TownUiState(
    val isLoading: Boolean = false,
    val selectedTab: TownTab = TownTab.ALL,
    val townContent: TownContent = TownContent(isLoading = true, isNewUser = false, dataList = emptyList())
)

enum class TownTab {
    ALL,
    MY_POSTS
}

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

sealed interface TownEvent {
    data class ShowPostOptions(val growthId: Long) : TownEvent
    data object ShowSnackBarReportGrowth: TownEvent
}
