package com.theophiluskibet.dtasks.data.mappers

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.helpers.asEpochMilliseconds

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate?.asEpochMilliseconds(),
    isCompleted = isCompleted,
    createdAt = createdAt.asEpochMilliseconds(),
    updatedAt = updatedAt.asEpochMilliseconds()
)