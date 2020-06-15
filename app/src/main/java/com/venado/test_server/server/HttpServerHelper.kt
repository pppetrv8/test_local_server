package com.venado.test_server.server

import android.content.Context
import com.venado.test_server.server.impl.NanoHttpdHttpServerHelper

interface HttpServerHelper {

    fun startServer(hostAddress: String?, port: Int)

    fun stopServer()

    fun clear()

    companion object {
         fun getHelper(context: Context) : HttpServerHelper {
             //return SunHttpServerHelper(context)
             return NanoHttpdHttpServerHelper(context)
         }
    }
}