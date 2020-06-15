package com.venado.test_server.server.impl

import android.content.Context
import com.venado.test_server.utils.AndroidUtil
import com.venado.test_server.utils.Constants.SERVER_API_CONTEXT
import com.venado.test_server.utils.Constants.SERVER_CONTENT_ROOT
import com.venado.test_server.utils.Constants.SERVER_FILES_CONTEXT
import fi.iki.elonen.NanoHTTPD
import timber.log.Timber
import java.io.File

class MyHTTPD(val context: Context, hostName: String?, httpPort: Int)
    : NanoHTTPD(hostName, httpPort) {

    override fun serve(session: IHTTPSession): Response? {
        val uri = session.uri
        Timber.d("request: ${session.method} $uri")
        if (uri == "/hello") {
            val response = "NanoHTTPD: HelloWorld"
            return newFixedLengthResponse(response)
        } else if (uri.startsWith(SERVER_FILES_CONTEXT)) {
            val filename = File(uri).name
            val mimeType = getMimeTypeForFile(uri)
            val fileStream = AndroidUtil.readAssetStream(context, filename)
            val response = newChunkedResponse(Response.Status.OK, mimeType, fileStream)
            return response
        } else if (uri.contains(SERVER_API_CONTEXT)) {
            val path = uri.substringAfter(SERVER_API_CONTEXT)
            //val filename = File(path).name
            val mimeType = getMimeTypeForFile(uri)
            val fileStream = AndroidUtil.readAssetStream(context, "$SERVER_CONTENT_ROOT$path")
            val response = newChunkedResponse(Response.Status.OK, mimeType, fileStream)
            return response
        }
        return null
    }
}