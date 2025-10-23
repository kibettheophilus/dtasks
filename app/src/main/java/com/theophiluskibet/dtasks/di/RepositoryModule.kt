package com.theophiluskibet.dtasks.di

import com.theophiluskibet.dtasks.data.repository.AuthRepositoryImpl
import com.theophiluskibet.dtasks.data.repository.SyncRepositoryImpl
import com.theophiluskibet.dtasks.data.repository.TasksRepositoryImpl
import com.theophiluskibet.dtasks.domain.repository.AuthRepository
import com.theophiluskibet.dtasks.domain.repository.SyncRepository
import com.theophiluskibet.dtasks.domain.repository.TasksRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for providing repositories.
 */
val repositoryModule = module {
    /**
     * Provides an instance of [TasksRepositoryImpl] as a [TasksRepository].
     */
    singleOf(::TasksRepositoryImpl) bind TasksRepository::class
    /**
     * Provides an instance of [AuthRepositoryImpl] as an [AuthRepository].
     */
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    /**
     * Provides an instance of [SyncRepositoryImpl] as a [SyncRepository].
     */
    singleOf(::SyncRepositoryImpl) bind SyncRepository::class
}
