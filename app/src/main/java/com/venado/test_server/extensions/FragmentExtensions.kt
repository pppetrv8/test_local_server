package com.venado.test_server.extensions

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.venado.test_server.ui.BaseFragment
import com.venado.test_server.ui.screen.Screen
import com.venado.test_server.ui.viewmodel.BaseViewModel


inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory?, body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory)[T::class.java]
    vm.body()
    return vm
}

inline fun <reified T : BaseViewModel<out Screen>> Fragment.viewModel(body: T.() -> Unit): T { return viewModel(null, body) }

//fun BaseFragment<out ViewDataBinding>.replaceFragment(fragment: Fragment, bundle: Bundle?) {
//    activity?.let {
//        if (it is BaseActivityScreen) {
//            it.replaceFragment(fragment, bundle)
//        }
//    }
//}

fun BaseFragment.findFragmentById(id: Int): Fragment? {
    return parentFragmentManager.findFragmentById(id)
}

fun BaseFragment.findChildFragmentById(id: Int): Fragment? {
    return childFragmentManager.findFragmentById(id)
}


inline fun <reified T> BaseFragment.getArgumentsData(key: String): T? {
    if (arguments?.containsKey(key) == true) {
        return arguments?.get(key)?.takeIf { p -> p is T } as T?
    }
    return null
}

fun BaseFragment.runIfActivityAlive(run: (FragmentActivity) -> Unit) {
    activity?.takeUnless { it.isFinishing || it.isDestroyed }
    .takeIf { isVisible && !isDetached }?.let { run(it) }
}

fun BaseFragment.closeActivity() = activity?.finish()

fun BaseFragment.close() = fragmentManager?.popBackStack()

fun BaseFragment.showToast(msg: String) = appContext?.showToast(msg)
fun BaseFragment.showToast(resId: Int) = appContext?.showToast(resId)
fun BaseFragment.showSnackBar(view: View?, resId: Int, indefinite: Boolean = false) = appContext?.showSnackBar(view, resId, indefinite)
fun BaseFragment.showSnackBar(view: View?, msg: String, indefinite: Boolean = false) = appContext?.showSnackBar(view, msg, indefinite)

val BaseFragment.appContext: Context? get() = activity?.applicationContext



