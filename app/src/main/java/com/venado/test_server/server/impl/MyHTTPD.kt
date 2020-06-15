package com.venado.test_server.server.impl

import android.content.Context
import com.venado.test_server.utils.AndroidUtil
import fi.iki.elonen.NanoHTTPD
import java.io.File

class MyHTTPD(val context: Context, hostName: String?, httpPort: Int)
    : NanoHTTPD(hostName, httpPort) {

    override fun serve(session: IHTTPSession): Response? {
        val uri = session.uri
        if (uri == "/hello") {
            val response = "NanoHTTPD: HelloWorld"
            return newFixedLengthResponse(response)
        } else if (uri.startsWith("/files")) {
            val filename = File(uri).name
            val mimeType = getMimeTypeForFile(uri)
            val fileStream = AndroidUtil.readAssetStream(context, filename)
            val response = newChunkedResponse(Response.Status.OK, mimeType, fileStream)
            return response
        }
        return null
    }
}