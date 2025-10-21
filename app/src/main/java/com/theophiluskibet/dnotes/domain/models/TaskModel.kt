package com.theophiluskibet.dnotes.domain.models

import java.util.Date

data class TaskModel(
    val id: String,
    val title: String,
    val description: String = "",
    val dueDate: Date?,
    val isCompleted: Boolean = false,
    val createdAt: String,
    val updatedAt: String
)
