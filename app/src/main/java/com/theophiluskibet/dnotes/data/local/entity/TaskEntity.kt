package com.theophiluskibet.dnotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val isComplete: Boolean = false,
    val updatedAt: String
)
