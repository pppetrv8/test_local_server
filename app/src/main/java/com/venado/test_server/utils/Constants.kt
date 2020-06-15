package com.venado.test_server.utils

object Constants {

    const val OK_CODE = 200
    const val HTTP_PORT = 8080

    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val HEADER_CONTENT_DISPOSITION = "Content-Disposition"
    const val CONTENT_DISPOSITION_PARAMS = "attachment; filename="

    const val SERVER_ROOT_CONTEXT = "/"
    const val SERVER_FILES_CONTEXT = SERVER_ROOT_CONTEXT + "files"
    const val SERVER_API_CONTEXT = SERVER_ROOT_CONTEXT + "api"
    const val SERVER_CONTENT_ROOT = "build"
}