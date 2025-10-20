package com.theophiluskibet.dnotes.di

import com.theophiluskibet.dnotes.data.repository.TasksRepositoryImpl
import com.theophiluskibet.dnotes.domain.repository.TasksRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TasksRepositoryImpl) bind TasksRepository::class
}