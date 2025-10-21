package com.theophiluskibet.dtasks.data.repository

import com.theophiluskibet.dtasks.data.local.dao.TasksDao
import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import com.theophiluskibet.dtasks.data.mappers.toDomain
import com.theophiluskibet.dtasks.data.mappers.toDto
import com.theophiluskibet.dtasks.data.mappers.toEntity
import com.theophiluskibet.dtasks.data.remote.api.TasksApi
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.SyncRepository
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

    override suspend fun pushTasks(): Result<Boolean> {
        return try {

            val response = tasksApi.syncTasks(tasks = getUnsyncedTasks().map { it.toDto() })

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

    override suspend fun updateLastSyncTime() {
        preferenceManager.updateLastSyncTime(timeStamp = 0L)
    }

    override suspend fun updateSyncedTasks(taskIds: List<String>) {
        taskIds.forEach { id ->
            tasksDao.getTaskById(id)?.let { task ->
                tasksDao.updateTask(task.copy(updatedAt = ""))
            }
        }
    }

    override suspend fun getUnsyncedTasks(): List<TaskEntity> {
        val lastSyncTime = preferenceManager.fetchLastSyncTime.first() ?: 0L

        return tasksDao.getTasksBySyncTime(lastSyncTime = lastSyncTime)
    }
}