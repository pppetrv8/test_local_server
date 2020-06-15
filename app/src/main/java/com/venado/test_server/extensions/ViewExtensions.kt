package com.venado.vcbsimulator.domain.extension

import android.view.View

val View.isVisible: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }

fun View.visible() { this.visibility = View.VISIBLE }

fun View.invisible() { this.visibility = View.INVISIBLE }

fun View.hide() { this.visibility = View.GONE }

fun View.setShow(show: Boolean) {
    if (show) {
        visible()
    } else {
        hide()
    }
}

fun View.setHide(hide: Boolean) {
    if (hide) {
        invisible()
    } else {
        visible()
    }
}