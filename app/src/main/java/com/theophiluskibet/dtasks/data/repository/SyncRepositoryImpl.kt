package com.theophiluskibet.dtasks.data.repository

import android.util.Log
import com.theophiluskibet.dtasks.data.local.dao.TasksDao
import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import com.theophiluskibet.dtasks.data.mappers.toDomain
import com.theophiluskibet.dtasks.data.mappers.toDto
import com.theophiluskibet.dtasks.data.mappers.toEntity
import com.theophiluskibet.dtasks.data.remote.api.TasksApi
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.SyncRepository
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import com.theophiluskibet.dtasks.helpers.asEpochMilliseconds
import com.theophiluskibet.dtasks.helpers.asLocalDateTime
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SyncRepositoryImpl(
    private val tasksApi: TasksApi,
    private val tasksDao: TasksDao,
    private val preferenceManager: PreferenceManager
) : SyncRepository {
    @OptIn(ExperimentalTime::class)
    override suspend fun fetchTasks(): Result<List<TaskModel>> {
        return try {
            val lastSyncTime = preferenceManager.fetchLastSyncTime.first()
                ?: Clock.System.now().LocalDateTime.asEpochMilliseconds()
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
                Result.success(true)
               // Result.failure(Exception("Sync failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.success(true)
            //Result.failure(e)
        }
    }

    override suspend fun mergeTasks(remoteTasks: List<TaskModel>) {
        remoteTasks.forEach { remoteTask ->
            val localTask = tasksDao.getTaskById(id = remoteTask.id)

            if (localTask == null) {
                tasksDao.insertTask(remoteTask.toEntity())
            } else {
                when {
                    remoteTask.updatedAt > localTask.updatedAt.asLocalDateTime() -> {
                        tasksDao.insertTask(remoteTask.toEntity())
                    }

                    else -> {
                        // keep the changes
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun updateLastSyncTime() {
        preferenceManager.updateLastSyncTime(timeStamp = Clock.System.now().LocalDateTime.asEpochMilliseconds())
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun updateSyncedTasks(taskIds: List<String>) {
        taskIds.forEach { id ->
            tasksDao.getTaskById(id)?.let { task ->
                tasksDao.updateTask(task.copy(updatedAt = Clock.System.now().LocalDateTime.asEpochMilliseconds()))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getUnsyncedTasks(): List<TaskEntity> {
        val lastSyncTime = preferenceManager.fetchLastSyncTime.first()
            ?: Clock.System.now().LocalDateTime.asEpochMilliseconds()

        return tasksDao.getTasksBySyncTime(lastSyncTime = lastSyncTime)
    }
}