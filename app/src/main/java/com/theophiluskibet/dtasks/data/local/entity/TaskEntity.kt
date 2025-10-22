package com.theophiluskibet.dtasks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String,
    val dueDate: Long?,
    val isCompleted: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)
