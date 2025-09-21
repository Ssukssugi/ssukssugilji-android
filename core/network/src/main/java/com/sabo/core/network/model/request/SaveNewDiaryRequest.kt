package com.sabo.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SaveNewDiaryRequest(
    val plantId: Long,
    val date: String,
    val careTypes: List<String>,
    val diary: String
)
