package com.theophiluskibet.dnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theophiluskibet.dnotes.data.local.dao.TasksDao
import com.theophiluskibet.dnotes.data.local.entity.TaskEntity

@Database(version = 0, entities = [TaskEntity::class], exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
}