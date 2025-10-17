package com.theophiluskibet.dnotes.di

import androidx.room.Room
import com.theophiluskibet.dnotes.data.local.database.TasksDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val localModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = TasksDatabase::class.java,
            name = "tasks_database"
        )
    }
}