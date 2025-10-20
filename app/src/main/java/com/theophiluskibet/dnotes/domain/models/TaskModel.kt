package com.theophiluskibet.dnotes.domain.models

data class TaskModel(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean,
    val updatedAt: String
)
