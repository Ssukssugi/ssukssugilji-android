package com.sabo.core.network.interceptor

import com.sabo.core.model.TokenProvider
import com.sabo.core.network.handler.TokenExpirationHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val tokenExpirationHandler: TokenExpirationHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().apply {
            runBlocking {
                val accessTokenJob = async { tokenProvider.getAccessToken() }
                val refreshTokenJob = async { tokenProvider.getRefreshToken() }
                addHeader("Cookie", "access=${accessTokenJob.await()}")
                addHeader("Cookie", "refresh=${refreshTokenJob.await()}")
            }
        }.build()

        val response = chain.proceed(newRequest)

        when (response.code) {
            HttpURLConnection.HTTP_OK -> {
                val cookies = response.headers("Set-Cookie").ifEmpty { null } ?: return response
                CoroutineScope(Dispatchers.IO).launch {
                    var newAccessToken: String? = null
                    var newRefreshToken: String? = null
                    cookies.forEach {
                        if (it.startsWith("access=")) {
                            newAccessToken = it.substringAfter("access=").substringBefore(";").ifEmpty { null }
                        } else if (it.startsWith("refresh=")) {
                            newRefreshToken = it.substringAfter("refresh=").substringBefore(";").ifEmpty { null }
                        }
                    }
                    val accessTokenJob = async {
                        newAccessToken?.let {
                            val existedAccessToken = tokenProvider.getAccessToken()
                            if (existedAccessToken != newAccessToken) {
                                tokenProvider.setAccessToken(it)
                            }
                        } ?: run {
                            withContext(Dispatchers.Main) {
                                tokenExpirationHandler.onExpired()
                            }
                        }
                    }
                    val refreshTokenJob = async {
                        newRefreshToken?.let {
                            val existedRefreshToken = tokenProvider.getRefreshToken()
                            if (existedRefreshToken != newRefreshToken) {
                                tokenProvider.setRefreshToken(it)
                            }
                        }
                    }
                    accessTokenJob.await()
                    refreshTokenJob.await()
                }
            }
            HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_FORBIDDEN -> {
                val cookies = response.headers("Set-Cookie").ifEmpty { null } ?: return response
                cookies.forEach {
                    if (it.startsWith("access=")) {
                        val token = it.substringAfter("access=").substringBefore(";")
                        if (token.isEmpty()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                tokenExpirationHandler.onExpired()
                            }
                        }
                        return@forEach
                    }
                }
            }
        }

        return response
    }
}