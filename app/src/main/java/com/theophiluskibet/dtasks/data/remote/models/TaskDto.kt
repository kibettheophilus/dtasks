package com.theophiluskibet.dtasks.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: String?,
    @SerialName("completed")
    val isCompleted: Boolean,
    val createdAt: String,
    val updatedAt: String
)