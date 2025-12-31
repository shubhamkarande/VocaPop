package com.vocapop.android

import android.app.Application
import com.vocapop.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class VocaPopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        initKoin {
            androidLogger()
            androidContext(this@VocaPopApplication)
            modules(com.vocapop.android.di.appModule)
        }
    }
}
