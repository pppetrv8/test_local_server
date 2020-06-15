package com.venado.test_server.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.venado.test_server.R
import com.venado.test_server.extensions.showToast
import com.venado.test_server.service.ServerService

class MainActivity : AppCompatActivity() {

    private var startedServer = false

    private var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab = findViewById<FloatingActionButton>(R.id.fab)

        startedServer = ServerService.isRunning
        setButtonState(startedServer)

        fab?.setOnClickListener { view ->
            if (startedServer) {
                runServer(false)
                showToast("Server stopping")
            } else {
                runServer(true)
                showToast("Server running")
            }
            startedServer = !startedServer
            setButtonState(startedServer)
        }
    }

    private fun setButtonState(state: Boolean) {
        val imageRes = if (state) {
            android.R.drawable.ic_media_pause
        } else android.R.drawable.ic_media_play
        fab?.setImageResource(imageRes)
    }

    private fun runServer(run: Boolean) {
        val i = Intent(applicationContext, ServerService::class.java)
        if (run) {
            startService(i)
        } else {
            stopService(i)
        }
    }
}