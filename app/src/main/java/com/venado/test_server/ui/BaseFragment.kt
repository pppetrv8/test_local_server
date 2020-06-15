package com.venado.test_server.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.venado.test_server.ui.screen.Screen
import com.venado.test_server.ui.viewmodel.BaseViewModel

import timber.log.Timber

abstract class BaseFragment : Fragment(), Screen {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        getBaseViewModel()?.setScreen(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        getBaseViewModel()?.removeObservers(this)
        getBaseViewModel()?.removeScreen()
    }

    override fun initViewModel() {}

    private fun getBaseViewModel(): BaseViewModel<Screen>? {
        var screen: BaseViewModel<Screen>? = null
        getViewModel()?.let {
            if (it is BaseViewModel<out Screen>) {
                screen = it as BaseViewModel<Screen>
            }
        }
        return screen
    }

    abstract fun getLayoutId(): Int

    override fun getScreenActivity(): FragmentActivity? {
        return activity
    }

    override fun handleError(t: Throwable?) {
        Timber.i("error: $t")
        showProgress(false)

    }
}
