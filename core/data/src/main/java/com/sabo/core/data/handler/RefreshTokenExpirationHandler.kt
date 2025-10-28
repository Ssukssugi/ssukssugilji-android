package com.sabo.core.data.handler

import com.sabo.core.datastore.AuthDataStore
import com.sabo.core.datastore.LocalDataStore
import com.sabo.core.navigator.NavigationEventHandler
import com.sabo.core.navigator.di.AuthNavigation
import com.sabo.core.network.handler.TokenExpirationHandler
import javax.inject.Inject

class RefreshTokenExpirationHandler @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val localDataStore: LocalDataStore,
    @AuthNavigation private val navigationEventHandler: NavigationEventHandler
): TokenExpirationHandler {
    override suspend fun onExpired() {
        authDataStore.clear()
        localDataStore.clear()
        navigationEventHandler.triggerRouteToNavigate()
    }
}