package com.theophiluskibet.dtasks.domain.repository

import com.theophiluskibet.dtasks.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun getTasks(): Flow<List<TaskModel>>?

    suspend fun insertTask(task: TaskModel)

    suspend fun updateTask(task: TaskModel)

    suspend fun deleteTask(id: String)
}