package com.theophiluskibet.dnotes.data.repository

import com.theophiluskibet.dnotes.data.local.preferences.PreferenceManager
import com.theophiluskibet.dnotes.data.remote.api.AuthApi
import com.theophiluskibet.dnotes.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AuthRepositoryImpl(private val authApi: AuthApi, private val preferenceManager: PreferenceManager) :
    AuthRepository {
    override suspend fun login(email: String): Flow<String> {
        val result = authApi.login(email = email).body() ?: ""
        preferenceManager.updateToken(result)
        return flowOf(result)
    }
}