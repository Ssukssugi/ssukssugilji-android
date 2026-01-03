package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.data.handleResult
import com.sabo.core.datastore.LocalDataStore
import com.sabo.core.network.model.request.SaveGrowthRequest
import com.sabo.core.network.model.response.GetTownGrowth
import com.sabo.core.network.service.TownService
import javax.inject.Inject

class DefaultTownRepository @Inject constructor(
    private val townService: TownService,
    private val localDataStore: LocalDataStore
) : TownRepository {

    override suspend fun getTownGrowth(lastGrowthId: Long?): Result<GetTownGrowth> = handleResult(
        execute = {
            townService.getTownGrowth(lastGrowthId)
        },
        transform = { it }
    )

    override suspend fun getMyGrowth(): Result<GetTownGrowth> = handleResult(
        execute = {
            townService.getMyGrowth()
        },
        transform = { it }
    )

    override suspend fun reportTown(growthId: Long) = handleResult(
        execute = {
            townService.reportGrowth(growthId)
        },
        transform = {}
    )

    override suspend fun saveGrowth(beforeId: Long, afterId: Long): Result<Unit> = handleResult(
        execute = {
            townService.saveGrowth(
                request = SaveGrowthRequest(beforeDiaryId = beforeId, afterDiaryId = afterId)
            )
        },
        transform = {}
    )

    override suspend fun deleteGrowth(growthId: Long): Result<Unit> = handleResult(
        execute = {
            townService.deleteGrowth(growthId = growthId)
        },
        transform = {}
    )

    override suspend fun getUserId(): Long {
        return localDataStore.getUserId() ?: -1L
    }
}