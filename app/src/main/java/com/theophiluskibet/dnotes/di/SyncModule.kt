package com.theophiluskibet.dnotes.di

import com.theophiluskibet.dnotes.sync.SyncTasksWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val syncModule = module {
    workerOf(::SyncTasksWorker)
}