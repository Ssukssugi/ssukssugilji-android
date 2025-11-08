package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.network.model.response.GetTownGrowth

interface TownRepository {
    suspend fun getTownGrowth(lastGrowthId: Long? = null): Result<GetTownGrowth>
    suspend fun getMyGrowth(): Result<GetTownGrowth>
}