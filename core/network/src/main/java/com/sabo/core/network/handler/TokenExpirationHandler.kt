package com.sabo.core.network.handler

interface TokenExpirationHandler {
    suspend fun onExpired()
}