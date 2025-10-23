package com.theophiluskibet.dtasks

import android.app.Application
import com.theophiluskibet.dtasks.di.localModule
import com.theophiluskibet.dtasks.di.networkModule
import com.theophiluskibet.dtasks.di.repositoryModule
import com.theophiluskibet.dtasks.di.syncModule
import com.theophiluskibet.dtasks.di.viewModelModule
import com.theophiluskibet.dtasks.sync.schedulePeriodicSync
import com.theophiluskibet.dtasks.sync.triggerImmediateSync
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class DTasksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DTasksApp)
            modules(
                listOf(
                    localModule, networkModule, repositoryModule, syncModule,
                    viewModelModule
                )
            )
            workManagerFactory()

            schedulePeriodicSync()
        }
    }
}