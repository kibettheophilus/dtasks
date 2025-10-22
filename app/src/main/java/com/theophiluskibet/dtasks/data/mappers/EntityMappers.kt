package com.theophiluskibet.dtasks.data.mappers

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.data.remote.models.TaskDto
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.helpers.asLocalDateTime
import kotlinx.datetime.Instant

fun TaskEntity.toDomain() = TaskModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate?.asLocalDateTime(),
    isCompleted = isCompleted,
    createdAt = createdAt.asLocalDateTime(),
    updatedAt = updatedAt.asLocalDateTime()
)

fun TaskEntity.toDto() = TaskDto(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate?.let { timestamp ->
        Instant.fromEpochMilliseconds(timestamp)
            .toString() // This will format as ISO-8601
    },
    isCompleted = isCompleted,
    createdAt = Instant.fromEpochMilliseconds(createdAt).toString(),
    updatedAt = Instant.fromEpochMilliseconds(updatedAt).toString()
)
