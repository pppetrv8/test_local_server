package com.venado.test_server.server.impl

import android.content.Context
import com.venado.test_server.server.HttpServerHelper

class NanoHttpdHttpServerHelper(val context: Context): HttpServerHelper {

    var myHTTPD: MyHTTPD? = null

    override fun startServer(hostAddress: String?, port: Int) {
        if (myHTTPD == null) {
            myHTTPD = MyHTTPD(context, hostAddress, port)
        }
        myHTTPD?.start()
    }

    override fun stopServer() {
        myHTTPD?.stop()
    }

    override fun clear() {
        stopServer()
        myHTTPD = null
    }
}