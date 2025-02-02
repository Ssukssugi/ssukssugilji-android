package com.sabo.core.data

import com.sabo.core.domain.Result
import com.sabo.core.network.BaseResponse
import retrofit2.HttpException

suspend fun <T: Any, R: Any> handleResult(execute: suspend () -> BaseResponse<T>, transform: (T) -> R): Result<R> {
    return try {
        val response = execute()
        if (response.error != null) {
            Result.Error(
                code = response.error?.code,
                message = response.error?.message
            )
        } else {
            val body = response.data
            if (body != null) {
                Result.Success(transform(body))
            } else {
                Result.Error(
                    code = null,
                    message = "Response body is null"
                )
            }
        }
    } catch (e: HttpException) {
      Result.Error(
          code = e.code().toString(),
          message = e.message()
      )
    } catch (e: Throwable) {
        Result.Error(
            code = null,
            message = e.message
        )
    }
}