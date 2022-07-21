package com.asta.door.controller

import android.app.Application
import com.asta.door.controller.di.appModule
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        self = this
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(appModule)
        }
    }

    companion object {
        lateinit var self: App
        val packageId: String by lazy { self.packageManager.getPackageInfo(self.packageName, 0).packageName }
    }
}