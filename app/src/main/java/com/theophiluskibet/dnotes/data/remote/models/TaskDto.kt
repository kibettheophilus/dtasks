package com.theophiluskibet.dnotes.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val updatedAt: String
)