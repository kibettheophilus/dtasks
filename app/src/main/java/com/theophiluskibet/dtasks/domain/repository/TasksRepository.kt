package com.theophiluskibet.dtasks.domain.repository

import com.theophiluskibet.dtasks.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

/**
 * An interface for handling tasks.
 */
interface TasksRepository {
    /**
     * Gets a [Flow] of all tasks.
     *
     * @return A [Flow] of a list of [TaskModel]s.
     */
    suspend fun getTasks(): Flow<List<TaskModel>>?

    /**
     * Inserts a task.
     *
     * @param task The task to insert.
     */
    suspend fun insertTask(task: TaskModel)

    /**
     * Updates a task.
     *
     * @param task The task to update.
     */
    suspend fun updateTask(task: TaskModel)

    /**
     * Deletes a task.
     *
     * @param id The ID of the task to delete.
     */
    suspend fun deleteTask(id: String)
}
