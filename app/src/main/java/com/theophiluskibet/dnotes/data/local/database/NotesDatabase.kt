package com.theophiluskibet.dnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theophiluskibet.dnotes.data.local.dao.NotesDao
import com.theophiluskibet.dnotes.data.local.entity.NoteEntity

@Database(version = 0, entities = [NoteEntity::class], exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}