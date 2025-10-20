package com.theophiluskibet.dnotes.domain.repository

import com.theophiluskibet.dnotes.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun getTasks(): Flow<List<TaskModel>>

    suspend fun getTaskById(id: String): TaskModel

    suspend fun insertTask(task: TaskModel)

    suspend fun updateTask(task: TaskModel)

    suspend fun deleteTask(id: String)
}