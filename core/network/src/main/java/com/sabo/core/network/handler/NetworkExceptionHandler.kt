package com.sabo.core.network.handler

import com.sabo.core.model.NetworkError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException

object NetworkExceptionHandler {

    fun mapToNetworkError(throwable: Throwable): NetworkError {
        return when (throwable) {
            is UnknownHostException -> NetworkError.NoInternet()
            is SocketTimeoutException -> NetworkError.Timeout()
            is ConnectException -> NetworkError.NoInternet("no_internet_connection")
            is HttpException -> mapHttpException(throwable)
            is IOException -> {
                if (isNetworkRelated(throwable)) {
                    NetworkError.NoInternet()
                } else {
                    NetworkError.Unknown(cause = throwable)
                }
            }

            else -> NetworkError.Unknown(cause = throwable)
        }
    }

    private fun mapHttpException(exception: HttpException): NetworkError {
        return when (val code = exception.code()) {
            in 500..599 -> NetworkError.ServerError(
                code = code,
                message = "Internal Server Error"
            )

            in 400..499 -> NetworkError.ServerError(
                code = code,
                message = exception.message() ?: "Request Error"
            )

            else -> NetworkError.Unknown(cause = exception)
        }
    }

    private fun isNetworkRelated(exception: IOException): Boolean {
        val message = exception.message?.lowercase() ?: return false
        return message.contains("network") ||
                message.contains("connection") ||
                message.contains("timeout") ||
                message.contains("unreachable") ||
                message.contains("reset") ||
                message.contains("closed")
    }
}
