package com.theophiluskibet.dtasks.di

import com.theophiluskibet.dtasks.sync.SyncTasksWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val syncModule = module {
    workerOf(::SyncTasksWorker)
}