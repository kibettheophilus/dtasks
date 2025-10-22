package com.theophiluskibet.dtasks.data.mappers

import com.theophiluskibet.dtasks.data.remote.models.TaskDto
import com.theophiluskibet.dtasks.domain.models.TaskModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun TaskDto.toDomain() = TaskModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate?.let { dateString ->
        try {
            Instant.parse(dateString).toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (e: Exception) {
            null
        }
    },
    isCompleted = isCompleted,
    createdAt = Instant.parse(createdAt).toLocalDateTime(TimeZone.currentSystemDefault()),
    updatedAt = Instant.parse(updatedAt).toLocalDateTime(TimeZone.currentSystemDefault())
)