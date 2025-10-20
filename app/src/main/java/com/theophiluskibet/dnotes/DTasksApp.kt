package com.theophiluskibet.dnotes

import android.app.Application
import com.theophiluskibet.dnotes.di.localModule
import com.theophiluskibet.dnotes.di.networkModule
import com.theophiluskibet.dnotes.di.repositoryModule
import org.koin.core.context.startKoin

class DTasksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(localModule, networkModule, repositoryModule))
        }
    }
}