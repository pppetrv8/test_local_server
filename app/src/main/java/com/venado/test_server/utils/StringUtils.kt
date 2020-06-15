package com.venado.test_server.utils

import java.io.InputStream
import java.util.*

object StringUtils {

    fun streamToString(inputStream: InputStream): String {
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

}