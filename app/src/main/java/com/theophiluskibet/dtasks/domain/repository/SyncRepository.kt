package com.theophiluskibet.dtasks.domain.repository

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel

interface SyncRepository {
    suspend fun fetchTasks(): Result<List<TaskModel>>
    suspend fun pushTasks(): Result<Boolean>
    suspend fun mergeTasks(remoteTasks: List<TaskModel>)
    suspend fun updateLastSyncTime()
    suspend fun updateSyncedTasks(taskIds: List<String>)
    suspend fun getUnsyncedTasks(): List<TaskEntity>
}