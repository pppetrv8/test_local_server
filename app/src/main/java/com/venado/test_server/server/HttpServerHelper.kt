package com.venado.test_server.server

import android.content.Context
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import com.venado.test_server.utils.AndroidUtil.readAsset
import com.venado.test_server.utils.Constants.CONTENT_TYPE_HTML
import com.venado.test_server.utils.Constants.CONTENT_TYPE_JSON
import com.venado.test_server.utils.Constants.CONTENT_TYPE_MP4
import com.venado.test_server.utils.Constants.HEADER_CONTENT_TYPE
import com.venado.test_server.utils.Constants.OK_CODE
import com.venado.test_server.utils.Constants.SERVER_FILES_CONTEXT
import com.venado.test_server.utils.Constants.SERVER_ROOT_CONTEXT
import java.io.IOException
import java.net.InetSocketAddress
import java.util.concurrent.Executors

class HttpServerHelper(val context: Context) {

    private var mHttpServer: HttpServer? = null
    private val executor = Executors.newCachedThreadPool()

    // Handler for root endpoint
    private val rootHandler = HttpHandler { exchange ->
        run {
            // Get request method
            when (exchange?.requestMethod) {
                "GET" -> {
                    val uri = exchange?.requestURI
                    if (uri.path == SERVER_FILES_CONTEXT) {
                        //sendFileResponse(exchange, "test_video.mp4", CONTENT_TYPE_MP4)
                        //sendFileResponse(exchange, "def_vcb_localizations.json", CONTENT_TYPE_JSON)
                        sendFileResponse(exchange, "html_test.html", CONTENT_TYPE_HTML)
                    } else sendResponse(exchange, "Welcome to my server")
                }
            }
        }
    }

    fun startServer(hostAddress: String?, port: Int) {
        if (!executor.isShutdown) {
            executor.execute { startServerMain(hostAddress, port) }
        }
    }

    fun stopServer() {
        if (!executor.isShutdown) {
            executor.execute { stopServerMain() }
        }
    }

    fun clear() {
        executor.shutdown()
    }

    private fun startServerMain(hostAddress: String?, port: Int) {
        try {
            mHttpServer = HttpServer.create(InetSocketAddress(hostAddress, port), 0)
            mHttpServer?.executor = Executors.newCachedThreadPool()
            mHttpServer?.createContext(SERVER_ROOT_CONTEXT, rootHandler)
            // 'this' refers to the handle method
            mHttpServer?.createContext(SERVER_FILES_CONTEXT, rootHandler)
            mHttpServer?.start() //start server;
            println("Server is running on ${mHttpServer?.address}:$port")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopServerMain() {
        mHttpServer?.stop(0)
    }


    private fun sendFileResponse(httpExchange: HttpExchange, fileName: String, mimeType: String) {
        val os = httpExchange.responseBody
        val fileStr = readAsset(context, fileName)
        val fileBytes = fileStr.toByteArray()
        httpExchange.setAttribute(HEADER_CONTENT_TYPE, mimeType)
        httpExchange.responseHeaders.add("Content-Disposition", "attachment; filename=" + fileName)
        httpExchange.sendResponseHeaders(OK_CODE, fileBytes.size.toLong())
        os.write(fileBytes)
        os.flush()
        os.close()
    }

    private fun sendResponse(httpExchange: HttpExchange, responseText: String){
        httpExchange.sendResponseHeaders(OK_CODE, responseText.length.toLong())
        val os = httpExchange.responseBody
        os.write(responseText.toByteArray())
        os.close()
    }
}