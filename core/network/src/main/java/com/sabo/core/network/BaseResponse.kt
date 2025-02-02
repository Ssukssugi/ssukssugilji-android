package com.sabo.core.network

import kotlinx.serialization.Serializable

@Serializable
class BaseResponse<T>(
    val data: T?,
    val error: Error?
)

@Serializable
class Error(val code: String, val message: String)