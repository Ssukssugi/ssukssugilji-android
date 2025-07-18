package com.sabo.core.data

import retrofit2.HttpException
import retrofit2.Response

suspend fun <T: Any, R: Any> handleResult(execute: suspend () -> Response<T>, transform: (T) -> R): Result<R> {
    return try {
        val response = execute()
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Response body is null")
            Result.Success(transform(body))
        } else {
            Result.Error(
                code = response.code().toString(),
                message = response.message()
            )
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