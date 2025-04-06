package com.sabo.core.network.service

import com.sabo.core.network.model.request.ApplyUserDetailRequest
import com.sabo.core.network.model.request.CheckNicknameRequest
import com.sabo.core.network.model.response.CheckNicknameResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {

    @POST("/api/v1/users/dup-nickname")
    suspend fun checkNickname(
        @Body request: CheckNicknameRequest
    ): Response<CheckNicknameResponse>

    @POST("/api/v1/users/details")
    suspend fun applyUserDetail(
        @Body request: ApplyUserDetailRequest
    ): Response<Unit>
}