package com.theophiluskibet.dnotes.di

import com.theophiluskibet.dnotes.presentation.login.LoginViewModel
import com.theophiluskibet.dnotes.presentation.tasks.TasksViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::TasksViewModel)
    viewModelOf(::LoginViewModel)
}