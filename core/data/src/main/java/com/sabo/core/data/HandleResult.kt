package com.sabo.core.data

import com.sabo.core.model.NetworkError
import com.sabo.core.network.handler.NetworkExceptionHandler
import retrofit2.HttpException
import retrofit2.Response

/**
 * API 호출을 처리하고 Result로 변환하는 함수
 * 네트워크 에러 발생 시 NetworkError로 매핑합니다.
 *
 * @param execute API 호출 실행
 * @param transform 응답 변환
 * @return Result<R> - 성공 시 Success, 실패 시 Error (NetworkError 포함)
 */
suspend fun <T: Any, R: Any> handleResult(
    execute: suspend () -> Response<T>,
    transform: (T) -> R
): Result<R> {
    return try {
        val response = execute()
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Response body is null")
            Result.Success(transform(body))
        } else {
            val networkError = NetworkError.ServerError(
                code = response.code(),
                message = response.message() ?: "Internal Server Error"
            )
            Result.Error(
                code = response.code().toString(),
                message = response.message(),
                exception = networkError
            )
        }
    } catch (e: HttpException) {
        val networkError = NetworkExceptionHandler.mapToNetworkError(e)
        Result.Error(
            code = e.code().toString(),
            message = networkError.message,
            exception = networkError
        )
    } catch (e: Throwable) {
        val networkError = NetworkExceptionHandler.mapToNetworkError(e)
        Result.Error(
            code = null,
            message = networkError.message,
            exception = networkError
        )
    }
}