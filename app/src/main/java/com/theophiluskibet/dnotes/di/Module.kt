package com.theophiluskibet.dnotes.di

import androidx.room.Room
import com.theophiluskibet.dnotes.data.local.database.NotesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val localModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = NotesDatabase::class.java,
            name = "notes_database"
        )
    }
}