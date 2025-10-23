package com.theophiluskibet.dtasks.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Manages user preferences and settings using [DataStore].
 *
 * @param dataStore The [DataStore] instance for storing preferences.
 */
class PreferenceManager(private val dataStore: DataStore<Preferences>) {

    /**
     * A [Flow] that emits the user's authentication token.
     */
    val fetchToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    /**
     * Updates the user's authentication token.
     *
     * @param token The new token.
     */
    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    /**
     * A [Flow] that emits the last sync timestamp in milliseconds.
     */
    val fetchLastSyncTime: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[LAST_SYNC_TIME_KEY]
    }

    /**
     * Updates the last sync timestamp.
     *
     * @param timeStamp The new timestamp in milliseconds.
     */
    suspend fun updateLastSyncTime(timeStamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME_KEY] = timeStamp
        }
    }

    /**
     * A [Flow] that emits the user's login status.
     */
    val isLoggedIn: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    /**
     * Updates the user's login status.
     *
     * @param isLoggedIn The new login status.
     */
    suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    private companion object Companion {
        // Key for storing the authentication token.
        val TOKEN_KEY = stringPreferencesKey("auth_token")

        // Key for storing the last sync timestamp.
        val LAST_SYNC_TIME_KEY = longPreferencesKey("last_sync_time")

        // Key for storing the user's login status.
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }
}
