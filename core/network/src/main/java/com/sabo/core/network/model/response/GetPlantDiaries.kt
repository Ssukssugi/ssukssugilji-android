package com.sabo.core.network.model.response

import com.sabo.core.model.CareType
import kotlinx.serialization.SerialName
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
            @SerialName("content")
            val contentRaw: String,
            val cares: List<CareType>
        ) {
            val content: String
                get() = contentRaw.replace("\\n", "\n")
        }
    }
}
