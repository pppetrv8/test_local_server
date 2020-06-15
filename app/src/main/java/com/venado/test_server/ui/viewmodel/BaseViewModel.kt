package com.venado.test_server.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.venado.test_server.ui.App
import com.venado.test_server.ui.screen.Screen

import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseViewModel<S : Screen> : ViewModel() {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var screen: WeakReference<S>? = null
    var errLiveData: MutableLiveData<Throwable> = MutableLiveData()

    var isPaused: Boolean? = false

    val context: Context?
        get() {
            return App.appInstance
        }

    protected fun handleError(e: Throwable) {
        errLiveData.postValue(e)
    }

    open fun onStarted() {}


    open fun onPause() {
        isPaused = true
    }

    open fun onResume() {
        isPaused = false
    }

    fun setScreen(screen: S) {
        this.screen = WeakReference(screen)
    }

    fun removeScreen() {
        this.screen = null
    }

    open fun removeObservers(owner: LifecycleOwner) {
        cleanLiveData(errLiveData, owner)
        errLiveData = MutableLiveData()
    }

    private fun <T> cleanLiveData(liveData: MutableLiveData<T>, owner: LifecycleOwner) {
        liveData.removeObservers(owner)
        liveData.value = null
    }

    override fun onCleared() {
        Timber.i("${this::class.java.simpleName}: onCleared")
        compositeDisposable.clear()
        compositeDisposable.dispose()
        this.screen = null
        super.onCleared()
    }
}
