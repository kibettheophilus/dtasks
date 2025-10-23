package com.theophiluskibet.dtasks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.theophiluskibet.dtasks.data.local.database.TasksDatabase
import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module for providing local data sources.
 */
val localModule = module {
    /**
     * Provides a singleton instance of the [TasksDatabase].
     */
    single { createDatabase(get()) }

    /**
     * Provides a singleton instance of the [DataStore] for [Preferences].
     */
    single { createDatastore(get()) }

    /**
     * Provides a singleton instance of the [TasksDao].
     */
    single { get<TasksDatabase>().tasksDao() }

    /**
     * Provides a singleton instance of the [PreferenceManager].
     */
    singleOf(::PreferenceManager)
}

/**
 * Creates a new instance of the [TasksDatabase].
 *
 * @param context The application context.
 * @return A new instance of [TasksDatabase].
 */
private fun createDatabase(context: Context): TasksDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = TasksDatabase::class.java,
        name = "tasks_database"
    )
        .build()
}

/**
 * Creates a new instance of the [DataStore] for [Preferences].
 *
 * @param context The application context.
 * @return A new instance of [DataStore<Preferences>].
 */
private fun createDatastore(context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { context.filesDir.resolve("tasks.preferences_pb").absolutePath.toPath() },
    )
