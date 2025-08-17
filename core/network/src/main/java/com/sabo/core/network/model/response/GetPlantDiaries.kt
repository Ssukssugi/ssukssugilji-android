package com.sabo.core.network.model.response

import com.sabo.core.model.CareType
import kotlinx.serialization.Serializable

@Serializable
data class GetPlantDiaries(
    val byMonth: List<MonthlyContent>
) {
    @Serializable
    data class MonthlyContent(
        val year: Int,
        val month: Int,
        val diaries: List<Diary>
    ) {
        @Serializable
        data class Diary(
            val diaryId: Long,
            val date: String,
            val image: String,
            val content: String,
            val cares: List<CareType>
        )
    }
}
