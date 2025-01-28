package com.example.shows

import android.app.Application
import com.example.shows.di.AppModule.appModule
import com.example.shows.di.detailsModule
import com.example.shows.di.networkModule
import com.example.shows.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule,repositoryModule, detailsModule,networkModule)
        }
    }
}