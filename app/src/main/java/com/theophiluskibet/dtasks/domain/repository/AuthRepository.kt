package com.theophiluskibet.dtasks.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * An interface for handling authentication.
 */
interface AuthRepository {
    /**
     * Logs in a user.
     *
     * @param email The user's email address.
     * @return A [Result] containing `true` if the login was successful, `false` otherwise.
     */
    suspend fun login(email: String): Result<Boolean>

    /**
     * A [Flow] that emits the user's authentication status.
     * Emits `true` if the user is logged in, and `false` otherwise.
     */
    val isLoggedIn: Flow<Boolean?>
}
