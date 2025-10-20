package com.theophiluskibet.dnotes.data.repository

import com.theophiluskibet.dnotes.data.local.dao.TasksDao
import com.theophiluskibet.dnotes.data.mappers.toDomain
import com.theophiluskibet.dnotes.data.mappers.toEntity
import com.theophiluskibet.dnotes.domain.models.TaskModel
import com.theophiluskibet.dnotes.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(private val tasksDao: TasksDao) : TasksRepository {
    override suspend fun getTasks(): Flow<List<TaskModel>> {
        val tasks = tasksDao.getTasks().map { list ->
            list.map { task ->
                task.toDomain()
            }
        }
        return tasks
    }

    override suspend fun getTaskById(id: String): TaskModel {
        // TODO: fix me 
        val task = tasksDao.getTaskById(id = id)!!.toDomain()
        return task
    }

    override suspend fun insertTask(task: TaskModel) {
        tasksDao.insertTask(task = task.toEntity())
    }

    override suspend fun updateTask(task: TaskModel) {
        tasksDao.updateTask(task = task.toEntity())
    }

    override suspend fun deleteTask(id: String) {
        tasksDao.deleteTaskById(id = id)
    }
}