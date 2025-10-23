package com.theophiluskibet.dtasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [TaskEntity] class.
 */
@Dao
interface TasksDao {
    /**
     * Inserts a task into the database.
     *
     * @param task The task to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    /**
     * Updates a task in the database.
     *
     * @param task The task to update.
     */
    @Update
    fun updateTask(task: TaskEntity)

    /**
     * Gets a task by its ID.
     *
     * @param id The ID of the task.
     * @return The [TaskEntity] with the given ID, or `null` if it doesn't exist.
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: String): TaskEntity?

    /**
     * Gets a [Flow] of all tasks.
     *
     * @return A [Flow] of a list of [TaskEntity]s.
     */
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     */
    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteTaskById(id: String)

    /**
     * Gets a list of tasks that have been updated since the last sync time.
     *
     * @param lastSyncTime The last sync time in milliseconds.
     * @return A list of [TaskEntity]s.
     */
    @Query("SELECT * FROM tasks WHERE updatedAt > :lastSyncTime")
    suspend fun getTasksBySyncTime(lastSyncTime: Long): List<TaskEntity>
}
