package com.theophiluskibet.dtasks.di

import com.theophiluskibet.dtasks.presentation.login.LoginViewModel
import com.theophiluskibet.dtasks.presentation.tasks.TasksViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::TasksViewModel)
    viewModelOf(::LoginViewModel)
}