package com.theophiluskibet.dtasks.di

import com.theophiluskibet.dtasks.sync.SyncTasksWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

/**
 * Koin module for providing WorkManager workers.
 */
val syncModule = module {
    /**
     * Provides an instance of [SyncTasksWorker].
     */
    workerOf(::SyncTasksWorker)
}
