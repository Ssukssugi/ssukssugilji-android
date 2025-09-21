package com.sabo.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SaveNewDiary(
    val diaryId: Long,
    val imageUrl: String,
    val name: String
)