package com.venado.test_server.ui.screen

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel

interface Screen {
    fun handleError(t: Throwable?)
    fun showProgress(cancelable: Boolean, show: Boolean)
    fun showProgress(show: Boolean)
    fun getScreenActivity(): FragmentActivity?
    fun initViewModel()
    fun getViewModel(): ViewModel?
}