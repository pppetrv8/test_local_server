package com.venado.test_server.utils

import java.io.File
import java.io.InputStream
import java.net.URLConnection
import java.util.*

object StringUtils {

    fun streamToString(inputStream: InputStream): String {
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    fun getMimeType(pathStr: String): String {
        val file = File(pathStr)
        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        return mimeType
    }


}