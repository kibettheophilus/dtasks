package com.theophiluskibet.dnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.theophiluskibet.dnotes.data.local.converters.DateConverter
import com.theophiluskibet.dnotes.data.local.dao.TasksDao
import com.theophiluskibet.dnotes.data.local.entity.TaskEntity

@Database(version = 1, entities = [TaskEntity::class], exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
}