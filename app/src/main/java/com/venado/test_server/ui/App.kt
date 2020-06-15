package com.venado.test_server.ui

import android.app.Application

open class App: Application() {

    companion object {
        @JvmStatic var appInstance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}
