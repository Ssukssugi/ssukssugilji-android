package com.sabo.core.data.provider

import com.sabo.core.datastore.AuthDataStore
import com.sabo.core.model.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenProvider @Inject constructor(
    private val authDataStore: AuthDataStore
) : TokenProvider {

    override suspend fun getAccessToken(): String? {
        return authDataStore.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return authDataStore.getRefreshToken()
    }

    override suspend fun setAccessToken(accessToken: String) {
        authDataStore.setAccessToken(accessToken)
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        authDataStore.setRefreshToken(refreshToken)
    }

}