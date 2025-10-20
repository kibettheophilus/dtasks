package com.theophiluskibet.dnotes.data.mappers

import com.theophiluskibet.dnotes.data.remote.models.TaskDto
import com.theophiluskibet.dnotes.domain.models.TaskModel

fun TaskDto.toDomain() = TaskModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)