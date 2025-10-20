package com.theophiluskibet.dnotes.data.mappers

import com.theophiluskibet.dnotes.data.local.entity.TaskEntity
import com.theophiluskibet.dnotes.domain.models.TaskModel

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    updatedAt = updatedAt
)