package com.arindom.stategenie

import android.app.Application
import com.arindom.stategenie.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UsersApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UsersApplication)
            modules(listOf(appModule))
        }
    }
}