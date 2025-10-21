package com.theophiluskibet.dnotes.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String): Result<Boolean>
}