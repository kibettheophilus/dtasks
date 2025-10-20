package com.theophiluskibet.dnotes.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenProvider(private val dataStore: DataStore<Preferences>) {

    val fetchToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    private companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }
}