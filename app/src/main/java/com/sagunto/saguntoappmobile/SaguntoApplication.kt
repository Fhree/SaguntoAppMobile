package com.sagunto.saguntoappmobile

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SaguntoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SaguntoApplication)
            modules(module)
        }
    }
}