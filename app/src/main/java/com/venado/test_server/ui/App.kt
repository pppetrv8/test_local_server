package com.venado.test_server.ui

import android.app.Application
import com.venado.test_server.BuildConfig
import timber.log.Timber
import java.util.logging.Logger

open class App: Application() {

    companion object {
        @JvmStatic var appInstance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
