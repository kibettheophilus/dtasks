package com.theophiluskibet.dtasks.data.mappers

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.data.remote.models.TaskDto
import com.theophiluskibet.dtasks.domain.models.TaskModel
import java.text.SimpleDateFormat
import java.util.*

fun TaskEntity.toDomain() = TaskModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TaskEntity.toDto() = TaskDto(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate?.let {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(it)
    } ?: "",
    isCompleted = isCompleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)