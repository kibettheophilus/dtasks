package com.theophiluskibet.dtasks.data.repository

import com.theophiluskibet.dtasks.data.local.dao.TasksDao
import com.theophiluskibet.dtasks.data.mappers.toDomain
import com.theophiluskibet.dtasks.data.mappers.toEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.TasksRepository
import com.theophiluskibet.dtasks.helpers.safeDbTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(
    private val tasksDao: TasksDao
) : TasksRepository {
    override suspend fun getTasks(): Flow<List<TaskModel>> {
        val tasks = tasksDao.getTasks().map { list ->
            list.map { task ->
                task.toDomain()
            }
        }
        return tasks
    }

    override suspend fun insertTask(task: TaskModel) {
        safeDbTransaction {
            tasksDao.insertTask(task = task.toEntity())
        }
    }

    override suspend fun updateTask(task: TaskModel) {
        safeDbTransaction {
            tasksDao.updateTask(task = task.toEntity())
        }
    }

    override suspend fun deleteTask(id: String) {
        safeDbTransaction {
            tasksDao.deleteTaskById(id = id)
        }
    }
}