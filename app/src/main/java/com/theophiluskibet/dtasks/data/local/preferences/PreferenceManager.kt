package com.theophiluskibet.dtasks.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceManager(private val dataStore: DataStore<Preferences>) {

    val fetchToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    val fetchLastSyncTime: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[LAST_SYNC_TIME_KEY]
    }

    suspend fun updateLastSyncTime(timeStamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME_KEY] = timeStamp
        }
    }

    val isLoggedIn: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY]
    }

    suspend fun updateIsLoggedIn(isLoggedIn: Boolean = false) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    private companion object Companion {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val LAST_SYNC_TIME_KEY = longPreferencesKey("last_sync_time")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }
}