package com.theophiluskibet.dnotes.data.mappers

import com.theophiluskibet.dnotes.data.local.entity.TaskEntity
import com.theophiluskibet.dnotes.data.remote.models.TaskDto
import com.theophiluskibet.dnotes.domain.models.TaskModel

fun TaskEntity.toDomain() = TaskModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    updatedAt = updatedAt
)

fun TaskEntity.toDto() = TaskDto(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    updatedAt = updatedAt
)