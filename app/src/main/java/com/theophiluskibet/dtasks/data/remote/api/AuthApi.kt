package com.theophiluskibet.dtasks.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API interface for authentication-related network requests.
 */
interface AuthApi {
    /**
     * Logs in a user.
     *
     * @param email The user's email address.
     * @return A [Response] containing the authentication token.
     */
    @POST("/auth/login")
    suspend fun login(@Body email: String): Response<String>
}
