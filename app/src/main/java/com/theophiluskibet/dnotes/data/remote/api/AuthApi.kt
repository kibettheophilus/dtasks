package com.theophiluskibet.dnotes.data.remote.api

import com.theophiluskibet.dnotes.data.remote.models.AuthResponse
import retrofit2.Response
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(email: String): Response<AuthResponse>
}