package com.theophiluskibet.dnotes.domain.repository

import com.theophiluskibet.dnotes.domain.models.TaskModel

interface SyncRepository {
    suspend fun fetchTasks(): Result<List<TaskModel>>
    suspend fun pushTasks(localTasks: List<TaskModel>): Result<Boolean>
    suspend fun mergeTasks(remoteTasks: List<TaskModel>)
}