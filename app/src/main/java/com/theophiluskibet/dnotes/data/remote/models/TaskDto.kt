package com.theophiluskibet.dnotes.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean,
    val createdAt: String,
    val updatedAt: String
)