package com.sabo.core.domain

sealed interface Result<out T> {
    data class Success<out T>(val data: T): Result<T>
    data class Error(val code: String?, val message: String?): Result<Nothing>
}

fun <T: Any> Result<T>.handle(
    onSuccess: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    try {
        when(this) {
            is Result.Success -> onSuccess?.invoke(data)
            is Result.Error -> onError?.invoke(Exception(message))
        }
    } catch (e: Throwable) {
        onError?.invoke(e)
    } finally {
        onFinish?.invoke()
    }
}