package com.sabo.core.data

import com.sabo.core.model.NetworkError

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(
        val code: String?,
        val message: String?,
        val exception: Throwable? = null
    ) : Result<Nothing> {
        val isNetworkError: Boolean
            get() = exception is NetworkError && exception.isNetworkRelated

        val networkError: NetworkError?
            get() = exception as? NetworkError
    }
}

/**
 * Result를 처리하는 확장 함수
 *
 * @param onSuccess 성공 시 호출
 * @param onError 에러 시 호출
 * @param onNetworkError 네트워크 에러 시 호출 (옵션) - 설정하지 않으면 onError로 전달
 * @param onFinish 성공/실패 상관없이 마지막에 호출
 */
suspend fun <T : Any> Result<T>.handle(
    onSuccess: (suspend (T) -> Unit)? = null,
    onError: (suspend (Throwable) -> Unit)? = null,
    onNetworkError: (suspend (NetworkError) -> Unit)? = null,
    onFinish: (suspend () -> Unit)? = null
) {
    try {
        when (this) {
            is Result.Success -> onSuccess?.invoke(data)
            is Result.Error -> {
                val error = exception ?: Exception(message)
                if (error is NetworkError && onNetworkError != null) {
                    onNetworkError.invoke(error)
                } else {
                    onError?.invoke(error)
                }
            }
        }
    } catch (e: Throwable) {
        onError?.invoke(e)
    } finally {
        onFinish?.invoke()
    }
}