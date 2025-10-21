package com.theophiluskibet.dnotes.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body email: String): Response<String>
}