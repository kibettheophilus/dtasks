package com.theophiluskibet.dnotes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.theophiluskibet.dnotes.data.local.database.TasksDatabase
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val localModule = module {
    singleOf(::createDatabase)
    singleOf(::createDatastore)
    single { get<TasksDatabase>().tasksDao() }
}

private fun createDatabase(context: Context): TasksDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = TasksDatabase::class.java,
        name = "tasks_database"
    )
        .build()
}

private fun createDatastore(context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { context.filesDir.resolve("tasks.preferences_pb").absolutePath.toPath() },
    )