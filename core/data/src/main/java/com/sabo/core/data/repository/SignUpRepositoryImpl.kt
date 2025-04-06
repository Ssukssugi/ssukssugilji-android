package com.sabo.core.data.repository

import com.sabo.core.data.handleResult
import com.sabo.core.domain.Result
import com.sabo.core.domain.model.CheckNickname
import com.sabo.core.domain.repository.SignUpRepository
import com.sabo.core.network.model.request.ApplyUserDetailRequest
import com.sabo.core.network.model.request.CheckNicknameRequest
import com.sabo.core.network.service.SignUpService
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val signUpService: SignUpService
) : SignUpRepository{
    override suspend fun checkNicknameDuplicated(nickname: String): Result<CheckNickname> = handleResult(
        execute = {
            val request = CheckNicknameRequest(nickname)
            signUpService.checkNickname(request)
        },
        transform = {
            CheckNickname(it.available)
        }
    )

    override suspend fun applyUserDetails(
        nickname: String,
        ageGroup: Long?,
        plantReason: Set<String>?,
        signUpPath: Set<String>?
    ): Result<Unit> = handleResult(
        execute = {
            val request = ApplyUserDetailRequest(
                nickname = nickname,
                ageGroup = ageGroup,
                plantReason = plantReason?.let { set ->
                    ApplyUserDetailRequest.PlantReason.entries.filter { it.name in set }
                },
                signUpPath = signUpPath?.let { set ->
                    ApplyUserDetailRequest.SinUpPath.entries.filter { it.name in set }
                }
            )
            signUpService.applyUserDetail(request)
        },
        transform = {

        }
    )
}