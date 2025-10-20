package com.theophiluskibet.dnotes

import android.app.Application
import com.theophiluskibet.dnotes.di.localModule
import com.theophiluskibet.dnotes.di.networkModule
import com.theophiluskibet.dnotes.di.repositoryModule
import com.theophiluskibet.dnotes.di.syncModule
import com.theophiluskibet.dnotes.sync.schedulePeriodicSync
import com.theophiluskibet.dnotes.sync.triggerImmediateSync
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class DTasksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DTasksApp)
            modules(listOf(localModule, networkModule, repositoryModule, syncModule))
            workManagerFactory()

            schedulePeriodicSync()
            triggerImmediateSync()
        }
    }
}