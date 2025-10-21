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

val repositoryModule = module {
    singleOf(::TasksRepositoryImpl) bind TasksRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::SyncRepositoryImpl) bind SyncRepository::class
}