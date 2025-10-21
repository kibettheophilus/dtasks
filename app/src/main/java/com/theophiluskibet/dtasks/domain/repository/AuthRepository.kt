package com.theophiluskibet.dtasks.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String): Result<Boolean>
}