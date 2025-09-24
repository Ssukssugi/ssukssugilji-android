package com.sabo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.sabo.core.datastore.di.LocalDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStore @Inject constructor(
    @LocalDataStore private val dataStore: DataStore<Preferences>
) {
    object PreferencesKey {
        val SERVICE_NOTIFICATION_ENABLED = booleanPreferencesKey("SERVICE_NOTIFICATION_ENABLED")
        val MARKETING_NOTIFICATION_ENABLED = booleanPreferencesKey("MARKETING_NOTIFICATION_ENABLED")
    }

    suspend fun getServiceNotificationEnabled(): Boolean? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.SERVICE_NOTIFICATION_ENABLED]
        }.firstOrNull()
    }

    suspend fun setServiceNotificationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.SERVICE_NOTIFICATION_ENABLED] = enabled
        }
    }

    suspend fun getMarketingNotificationEnabled(): Boolean? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.MARKETING_NOTIFICATION_ENABLED]
        }.firstOrNull()
    }

    suspend fun setMarketingNotificationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.MARKETING_NOTIFICATION_ENABLED] = enabled
        }
    }

    suspend fun saveUserSettings(serviceNotificationEnabled: Boolean, marketingNotificationEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.SERVICE_NOTIFICATION_ENABLED] = serviceNotificationEnabled
            preferences[PreferencesKey.MARKETING_NOTIFICATION_ENABLED] = marketingNotificationEnabled
        }
    }
}