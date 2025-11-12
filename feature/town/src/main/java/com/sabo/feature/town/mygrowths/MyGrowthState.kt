package com.sabo.feature.town.mygrowths

import com.sabo.core.network.model.response.GetTownGrowth
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class MyGrowthState(
    val isLoading: Boolean = false,
    val growthList: List<Growth> = emptyList(),
    val isEmpty: Boolean = false
)

data class Growth(
    val id: Long,
    val profile: String,
    val plantName: String,
    val nickName: String,
    val oldImage: String,
    val newImage: String,
    val dateDiff: Int
)

fun GetTownGrowth.GrowthContent.toPresentation(): Growth {
    val beforeDate = LocalDate.parse(before.date)
    val afterDate = LocalDate.parse(after.date)
    val dateDiff = ChronoUnit.DAYS.between(beforeDate, afterDate).toInt()

    return Growth(
        id = growthId,
        profile = plant.plantImage,
        plantName = plant.name,
        nickName = owner.nickname,
        oldImage = before.imageUrl,
        newImage = after.imageUrl,
        dateDiff = dateDiff
    )
}

sealed interface MyGrowthSideEffect