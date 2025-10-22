package com.theophiluskibet.dtasks.domain.models

import com.theophiluskibet.dtasks.helpers.LocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class TaskModel(
    val id: String,
    val title: String,
    val description: String = "",
    val dueDate: LocalDateTime?,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@OptIn(ExperimentalTime::class)
fun TaskModel.isDue(): Boolean {
    return dueDate?.let { it < Clock.System.now().LocalDateTime } ?: false
}
