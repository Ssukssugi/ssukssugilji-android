package com.sabo.core.data.repository

import com.sabo.core.data.Result
import com.sabo.core.data.handleResult
import com.sabo.core.datastore.AuthDataStore
import com.sabo.core.datastore.LocalDataStore
import com.sabo.core.network.model.request.UpdateUserSettingsRequest
import com.sabo.core.network.model.request.UserSettingsKey
import com.sabo.core.network.model.response.GetUserSettings
import com.sabo.core.network.service.ProfileService
import java.net.UnknownHostException
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
    private val profileService: ProfileService,
    private val localDataStore: LocalDataStore,
    private val authDataStore: AuthDataStore
) : ProfileRepository {
    override suspend fun getUserProfile() = handleResult(
        execute = { profileService.getUserProfile() },
        transform = { it }
    )

    override suspend fun getUserSettings(): Result<GetUserSettings> = try {
        val result = handleResult(
            execute = { profileService.getUserSettings() },
            transform = { it }
        )

        when (result) {
            is Result.Success -> {
                localDataStore.saveUserSettings(
                    serviceNotificationEnabled = result.data.receiveServiceNoti,
                    marketingNotificationEnabled = result.data.agreeToReceiveMarketing
                )
            }
            is Result.Error -> {
            }
        }

        result
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException,
            is java.net.ConnectException,
            is java.net.SocketTimeoutException,
            is java.io.IOException -> {
                val serviceNotificationEnabled = localDataStore.getServiceNotificationEnabled() ?: false
                val marketingNotificationEnabled = localDataStore.getMarketingNotificationEnabled() ?: false

                Result.Success(
                    GetUserSettings(
                        receiveServiceNoti = serviceNotificationEnabled,
                        agreeToReceiveMarketing = marketingNotificationEnabled
                    )
                )
            }

            else -> Result.Error(null, e.message)
        }
    }

    override suspend fun updateMarketingSettings(value: Boolean) = handleResult(
        execute = {
            profileService.updateUserSettings(UpdateUserSettingsRequest(UserSettingsKey.MARKETING, value)).also {
                localDataStore.setMarketingNotificationEnabled(value)
            }
        },
        transform = {}
    )

    override suspend fun updateServiceNotificationSettings(value: Boolean) = handleResult(
        execute = {
            profileService.updateUserSettings(UpdateUserSettingsRequest(UserSettingsKey.SERVICE_NOTIFICATION, value)).also {
                localDataStore.setServiceNotificationEnabled(value)
            }
        },
        transform = {}
    )

    override suspend fun logout(): Result<Unit> = try {
        authDataStore.clear()
        localDataStore.clear()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(null, e.message)
    }

    override suspend fun deleteUser(): Result<Unit> {
        return try {
            val result = handleResult(
                execute = { profileService.deleteUser() },
                transform = {}
            )

            val finalResult = if (result is Result.Success) {
                authDataStore.clear()
                localDataStore.clear()

                val token = authDataStore.getAccessToken()
                if (token != null) {
                    Result.Error(null, "Failed to clear authentication data")
                } else {
                    result
                }
            } else {
                result
            }
            finalResult
        } catch (e: Exception) {
            Result.Error(null, e.message)
        }
    }
}