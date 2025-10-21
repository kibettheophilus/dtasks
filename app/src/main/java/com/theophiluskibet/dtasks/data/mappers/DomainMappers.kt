package com.theophiluskibet.dtasks.data.mappers

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)