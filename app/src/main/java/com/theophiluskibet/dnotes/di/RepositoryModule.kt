package com.theophiluskibet.dnotes.di

import com.theophiluskibet.dnotes.data.repository.AuthRepositoryImpl
import com.theophiluskibet.dnotes.data.repository.SyncRepositoryImpl
import com.theophiluskibet.dnotes.data.repository.TasksRepositoryImpl
import com.theophiluskibet.dnotes.domain.repository.AuthRepository
import com.theophiluskibet.dnotes.domain.repository.SyncRepository
import com.theophiluskibet.dnotes.domain.repository.TasksRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TasksRepositoryImpl) bind TasksRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::SyncRepositoryImpl) bind SyncRepository::class
}