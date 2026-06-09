package com.yolo.core.data.auth

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.yolo.core.domain.auth.AuthInfo
import com.yolo.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSettingsApi::class)
class SettingsSessionStorage(
    private val settings: ObservableSettings
) : SessionStorage {

    private val flowSettings = settings.toFlowSettings()
    private val authInfoKey = "KEY_AUTH_INFO"

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return flowSettings.getStringOrNullFlow(authInfoKey).map { json ->
            json?.let { Json.decodeFromString(AuthInfo.serializer(), it) }
        }
    }

    override suspend fun set(info: AuthInfo?) {
        if (info == null) {
            settings.remove(authInfoKey)
        } else {
            settings.putString(authInfoKey, Json.encodeToString(AuthInfo.serializer(), info))
        }
    }
}
