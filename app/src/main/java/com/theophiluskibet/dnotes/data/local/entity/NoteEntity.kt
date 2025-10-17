package com.theophiluskibet.dnotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val isComplete: Boolean = false,
    val createdAt: String,
    val updatedAt: String
)
