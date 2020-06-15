package com.venado.test_server.server

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class ServerService: Service() {

    var httpServer : HttpServerHelper? = null

    companion object {
        var isRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)
        runForeground()
        httpServer = HttpServerHelper()
        httpServer?.startServer("localhost", 8080)
        isRunning = true
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        httpServer?.stopServer()
        httpServer?.clear()
        httpServer = null
        isRunning = false

    }

    private fun runForeground() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationSender(this, manager).
                            getAppNotification("Server started", "Service running in foreground", this, true)
        startForeground(APP_NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
    }
}