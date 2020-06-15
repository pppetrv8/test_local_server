package com.venado.test_server.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import com.venado.test_server.utils.StringUtils
import org.json.JSONObject
import java.io.IOException
import java.net.InetSocketAddress
import java.util.concurrent.Executors

class HttpServerHelper {

    private var mHttpServer: HttpServer? = null
    private val executor = Executors.newCachedThreadPool()

    // Handler for root endpoint
    private val rootHandler = HttpHandler { exchange ->
        run {
            // Get request method
            when (exchange?.requestMethod) {
                "GET" -> {
                    sendResponse(exchange, "Welcome to my server")
                }
            }
        }
    }

    private val messageHandler = HttpHandler { httpExchange ->
        run {
            when (httpExchange!!.requestMethod) {
                "GET" -> {
                    // Get all messages
                    sendResponse(httpExchange, "Would be all messages stringified json")
                }
                "POST" -> {
                    val inputStream = httpExchange.requestBody

                    val requestBody = StringUtils.streamToString(inputStream)
                    val jsonBody = JSONObject(requestBody)
                    // save message to database

                    //for testing
                    sendResponse(httpExchange, jsonBody.toString())
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
            mHttpServer?.createContext("/", rootHandler)
            // 'this' refers to the handle method
            mHttpServer?.createContext("/index", rootHandler)
            mHttpServer?.createContext("/messages", messageHandler)
            mHttpServer?.start() //start server;
            println("Server is running on ${mHttpServer?.address}:$port")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopServerMain() {
        mHttpServer?.stop(0)
    }


    private fun sendResponse(httpExchange: HttpExchange, responseText: String){
        httpExchange.sendResponseHeaders(200, responseText.length.toLong())
        val os = httpExchange.responseBody
        os.write(responseText.toByteArray())
        os.close()
    }
}