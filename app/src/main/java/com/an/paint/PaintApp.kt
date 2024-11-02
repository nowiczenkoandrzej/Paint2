package com.an.paint

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PaintApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PaintApp)
            modules(appModule)
        }
    }
}