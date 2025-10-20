package com.theophiluskibet.dnotes.data.repository

import com.theophiluskibet.dnotes.domain.models.TaskModel
import com.theophiluskibet.dnotes.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class TasksRepositoryImpl : TasksRepository {
    override suspend fun getTasks(): Flow<List<TaskModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(id: String): TaskModel {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(task: TaskModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: TaskModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: String) {
        TODO("Not yet implemented")
    }
}