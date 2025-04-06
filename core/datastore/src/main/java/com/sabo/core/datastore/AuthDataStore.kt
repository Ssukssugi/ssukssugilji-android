package com.sabo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    object PreferencesKey {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.ACCESS_TOKEN]
        }.firstOrNull()
    }

    suspend fun setAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.ACCESS_TOKEN] = token
        }
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.REFRESH_TOKEN]
        }.firstOrNull()
    }

    suspend fun setRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.REFRESH_TOKEN] = token
        }
    }
}