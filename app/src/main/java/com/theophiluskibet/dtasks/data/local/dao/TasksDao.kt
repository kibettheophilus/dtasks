package com.theophiluskibet.dtasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: String): TaskEntity?

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteTaskById(id: String)

    @Query("SELECT * FROM tasks WHERE updatedAt > :lastSyncTime")
    suspend fun getTasksBySyncTime(lastSyncTime: Long): List<TaskEntity>
}