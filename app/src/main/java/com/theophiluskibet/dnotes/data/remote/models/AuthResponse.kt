package com.theophiluskibet.dnotes.data.remote.models

data class AuthResponse(
    val token: String,
    val tokenExpiry: String,
    val refreshToken: String,
    val refreshTokenExpiry: String
)
