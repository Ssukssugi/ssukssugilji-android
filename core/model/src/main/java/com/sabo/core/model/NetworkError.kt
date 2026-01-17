package com.sabo.core.model

sealed class NetworkError : Exception() {

    data class NoInternet(
        override val message: String = "no_internet_connection"
    ) : NetworkError()

    data class Timeout(
        override val message: String = "timeout"
    ) : NetworkError()

    data class ServerError(
        val code: Int,
        override val message: String
    ) : NetworkError()

    data class Unknown(
        override val cause: Throwable? = null,
        override val message: String = "unknown_error"
    ) : NetworkError()

    val isNetworkRelated: Boolean
        get() = this is NoInternet || this is Timeout
}
