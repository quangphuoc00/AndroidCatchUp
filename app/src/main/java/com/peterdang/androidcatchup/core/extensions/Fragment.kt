package com.peterdang.androidcatchup.core.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.peterdang.androidcatchup.core.blueprints.BaseActivity
import com.peterdang.androidcatchup.core.blueprints.BaseFragment
import com.peterdang.androidcatchup.core.blueprints.BaseViewModel
import kotlinx.android.synthetic.main.activity_main.*

inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory)[T::class.java]
    vm.body()
    return vm
}

val <T : BaseViewModel> BaseFragment<T>.viewContainer: Fragment
    get() = (activity as BaseActivity).fragmentContainer

val <T : BaseViewModel> BaseFragment<T>.appContext: Context
    get() = activity?.applicationContext!!
