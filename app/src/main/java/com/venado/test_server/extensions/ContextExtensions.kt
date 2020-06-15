package com.venado.test_server.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.showToast(msgId: Int) {
    val msg = getString(msgId)
    showToast(msg)
}

fun Context.showSnackBar(view: View?, msg: String, indefinite: Boolean = false) {
    view?.let {
        val length = if (indefinite) {
            Snackbar.LENGTH_INDEFINITE
        } else {
            Snackbar.LENGTH_LONG
        }
        Snackbar.make(it, msg, length).show()
    }
}

fun Context.showSnackBar(view: View?, msgId: Int, indefinite: Boolean = false) {
    val msg = getString(msgId)
    showSnackBar(view, msg, indefinite)
}
