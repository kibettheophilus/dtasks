package com.theophiluskibet.dnotes.data.repository

import com.theophiluskibet.dnotes.data.local.dao.TasksDao
import com.theophiluskibet.dnotes.data.local.preferences.PreferenceManager
import com.theophiluskibet.dnotes.data.mappers.toDomain
import com.theophiluskibet.dnotes.data.mappers.toDto
import com.theophiluskibet.dnotes.data.mappers.toEntity
import com.theophiluskibet.dnotes.data.remote.api.TasksApi
import com.theophiluskibet.dnotes.domain.models.TaskModel
import com.theophiluskibet.dnotes.domain.repository.SyncRepository
import kotlinx.coroutines.flow.first

class SyncRepositoryImpl(
    private val tasksApi: TasksApi,
    private val tasksDao: TasksDao,
    private val preferenceManager: PreferenceManager
) : SyncRepository {
    override suspend fun fetchTasks(): Result<List<TaskModel>> {
        return try {
            val lastSyncTime = preferenceManager.fetchLastSyncTime.first() ?: 0L
            val response = tasksApi.getTasks(since = lastSyncTime)

            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch tasks: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun pushTasks(localTasks: List<TaskModel>): Result<Boolean> {
        return try {
            val lastSyncTime = preferenceManager.fetchLastSyncTime.first() ?: 0L

            val pending = tasksDao.getTasksBySyncTime(lastSyncTime = lastSyncTime)

            val response = tasksApi.syncTasks(tasks = pending.map { it.toDto() })

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Sync failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun mergeTasks(remoteTasks: List<TaskModel>) {
        remoteTasks.forEach { remoteTask ->
            val localTask = tasksDao.getTaskById(id = remoteTask.id)

            if (localTask == null) {
                tasksDao.insertTask(remoteTask.toEntity())
            } else {
                when {
                    remoteTask.updatedAt > localTask.updatedAt -> {
                        tasksDao.insertTask(remoteTask.toEntity())
                    }
                    else -> {
                        // keep the changes
                    }
                }
            }
        }
    }
}