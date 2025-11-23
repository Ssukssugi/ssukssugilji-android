package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.network.model.response.GetTownGrowth

interface TownRepository {
    suspend fun getTownGrowth(lastGrowthId: Long? = null): Result<GetTownGrowth>
    suspend fun getMyGrowth(): Result<GetTownGrowth>
    suspend fun reportTown(growthId: Long): Result<Unit>
    suspend fun saveGrowth(beforeId: Long, afterId: Long): Result<Unit>
    suspend fun deleteGrowth(growthId: Long): Result<Unit>
}