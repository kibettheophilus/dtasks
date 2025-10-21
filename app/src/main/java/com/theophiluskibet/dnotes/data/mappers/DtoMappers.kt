package com.theophiluskibet.dnotes.data.mappers

import com.theophiluskibet.dnotes.data.remote.models.TaskDto
import com.theophiluskibet.dnotes.domain.models.TaskModel
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