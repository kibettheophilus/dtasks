package com.theophiluskibet.dtasks.data.repository

import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import com.theophiluskibet.dtasks.data.remote.api.AuthApi
import com.theophiluskibet.dtasks.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val preferenceManager: PreferenceManager
) :
    AuthRepository {
    override suspend fun login(email: String): Result<Boolean> {
        return try {
            val response = authApi.login(email = email)
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    preferenceManager.updateToken(body ?: "")
                    preferenceManager.updateIsLoggedIn(isLoggedIn = true)
                    Result.success(true)
                }

                else -> {
                    Result.success(true)
                    //  Result.failure(Exception(response.message()))
                }
            }
        } catch (e: Exception) {
            // return Result.failure(e)
            return Result.success(true)
        }

    }
}