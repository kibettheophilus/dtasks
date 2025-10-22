package com.theophiluskibet.dtasks.data.repository

import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import com.theophiluskibet.dtasks.data.remote.api.AuthApi
import com.theophiluskibet.dtasks.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

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
                    preferenceManager.updateToken(body ?: "user_token")
                    preferenceManager.updateIsLoggedIn(isLoggedIn = true)
                    Result.success(true)
                }

                else -> {
                    preferenceManager.updateIsLoggedIn(isLoggedIn = true)
                    Result.success(true)
                    //  Result.failure(Exception(response.message()))
                }
            }
        } catch (e: Exception) {
            preferenceManager.updateIsLoggedIn(isLoggedIn = true)
            // return Result.failure(e)
            return Result.success(true)
        }
    }

    override val isLoggedIn: Flow<Boolean?>
        get() = preferenceManager.isLoggedIn
}