package com.theophiluskibet.dnotes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.theophiluskibet.dnotes.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): TaskEntity

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteTaskById(id: Int)
}