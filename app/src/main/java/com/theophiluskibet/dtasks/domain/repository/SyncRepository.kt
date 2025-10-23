package com.theophiluskibet.dtasks.domain.repository

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel

/**
 * An interface for handling syncing.
 */
interface SyncRepository {
    /**
     * Fetches tasks from the remote server.
     *
     * @return A [Result] containing a list of [TaskModel]s.
     */
    suspend fun fetchTasks(): Result<List<TaskModel>>

    /**
     * Pushes local changes to the remote server.
     *
     * @return A [Result] containing `true` if the push was successful, `false` otherwise.
     */
    suspend fun pushTasks(): Result<Boolean>

    /**
     * Merges remote tasks into the local database.
     *
     * @param remoteTasks The list of remote tasks to merge.
     */
    suspend fun mergeTasks(remoteTasks: List<TaskModel>)

    /**
     * Updates the last sync time.
     */
    suspend fun updateLastSyncTime()

    /**
     * Updates the synced status of a list of tasks.
     *
     * @param taskIds The list of task IDs to update.
     */
    suspend fun updateSyncedTasks(taskIds: List<String>)

    /**
     * Gets a list of unsynced tasks.
     *
     * @return A list of [TaskEntity]s.
     */
    suspend fun getUnsyncedTasks(): List<TaskEntity>
}
