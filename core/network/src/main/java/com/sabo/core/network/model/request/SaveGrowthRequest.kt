package com.sabo.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SaveGrowthRequest(
    val beforeDiaryId: Long,
    val afterDiaryId: Long
)
