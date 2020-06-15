package com.venado.test_server.server.impl

import android.content.Context
import android.net.Uri
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import com.venado.test_server.server.HttpServerHelper
import com.venado.test_server.utils.AndroidUtil.readAsset
import com.venado.test_server.utils.Constants.HEADER_CONTENT_TYPE
import com.venado.test_server.utils.Constants.OK_CODE
import com.venado.test_server.utils.Constants.SERVER_FILES_CONTEXT
import com.venado.test_server.utils.Constants.SERVER_ROOT_CONTEXT
import com.venado.test_server.utils.StringUtils.getMimeType
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.Executors

class SunHttpServerHelper(val context: Context):
    HttpServerHelper {

    private var mHttpServer: HttpServer? = null
    private val executor = Executors.newCachedThreadPool()

    // Handler for root endpoint
    private val rootHandler = HttpHandler { exchange ->
        run {
            // Get request method
            try {
                when (exchange?.requestMethod) {
                    "GET" -> {
                        val uri = exchange?.requestURI
                        if (uri.path.contains(SERVER_FILES_CONTEXT)) {
                            val uri1 = Uri.parse(uri.path)
                            val segments = uri1.pathSegments
                            val fileName = segments[segments.size - 1]
                            val mimeType = getMimeType(fileName)
                            sendFileResponse(exchange, fileName, mimeType)
                        } else sendResponse(exchange, "Welcome to my server")
                    }
                }
            } catch (t: Throwable) {
                println("rootHandler error: $t")
            }
        }
    }

    override fun startServer(hostAddress: String?, port: Int) {
        if (!executor.isShutdown) {
            executor.execute { startServerMain(hostAddress, port) }
        }
    }

    override fun stopServer() {
        if (!executor.isShutdown) {
            executor.execute { stopServerMain() }
        }
    }

    override fun clear() {
        executor.shutdown()
    }

    private fun startServerMain(hostAddress: String?, port: Int) {
        try {
            val serverAddress = if (hostAddress.isNullOrEmpty()) {
                InetAddress.getLocalHost().hostName
            } else hostAddress
            mHttpServer = HttpServer.create(InetSocketAddress(serverAddress, port), 0)
            mHttpServer?.executor = Executors.newCachedThreadPool()
            mHttpServer?.createContext(SERVER_ROOT_CONTEXT, rootHandler)
            // 'this' refers to the handle method
            mHttpServer?.createContext(SERVER_FILES_CONTEXT, rootHandler)
            mHttpServer?.start() //start server;
            println("Server is running on ${mHttpServer?.address}:$port")
        } catch (e: IOException) {
            println("startServer error: $e")
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
        httpExchange.setAttribute("Transfer-Encoding", "chunked")
        httpExchange.setAttribute("Connection", "keep-alive")
        //httpExchange.responseHeaders.add("Content-Disposition", "attachment; filename=" + fileName)
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