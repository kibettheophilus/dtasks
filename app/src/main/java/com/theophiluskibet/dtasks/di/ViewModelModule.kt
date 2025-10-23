package com.theophiluskibet.dtasks.di

import com.theophiluskibet.dtasks.presentation.main.MainViewModel
import com.theophiluskibet.dtasks.presentation.login.LoginViewModel
import com.theophiluskibet.dtasks.presentation.tasks.TasksViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for providing ViewModels.
 */
val viewModelModule = module {
    /**
     * Provides an instance of [MainViewModel].
     */
    viewModelOf(::MainViewModel)
    /**
     * Provides an instance of [TasksViewModel].
     */
    viewModelOf(::TasksViewModel)
    /**
     * Provides an instance of [LoginViewModel].
     */
    viewModelOf(::LoginViewModel)
}
