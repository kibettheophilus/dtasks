package com.theophiluskibet.dtasks.data.mappers

import com.theophiluskibet.dtasks.data.remote.models.TaskDto
import com.theophiluskibet.dtasks.domain.models.TaskModel
import java.text.SimpleDateFormat
import java.util.*

fun TaskDto.toDomain() = TaskModel(
    id = id,
    title = title,
    description = description,
    dueDate = try {
        if (dueDate.isNotEmpty()) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dueDate)
        } else null
    } catch (e: Exception) {
        null
    },
    isCompleted = isCompleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)