package com.theophiluskibet.dnotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String,
    val dueDate: Date?,
    val isCompleted: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
)
