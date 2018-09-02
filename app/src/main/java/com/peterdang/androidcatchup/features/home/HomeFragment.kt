package com.peterdang.androidcatchup.features.home

import android.os.Bundle
import android.view.View

import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.blueprints.BaseFragment
import com.peterdang.androidcatchup.core.extensions.fail
import com.peterdang.androidcatchup.core.extensions.observe
import com.peterdang.androidcatchup.core.extensions.viewModel
import com.peterdang.androidcatchup.core.navigation.MyNavigator
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeViewModel>() {

    @Inject
    internal lateinit var navigator: MyNavigator

    override fun layoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        viewModel = viewModel(viewModelFactory) {
            observe(navFragment, ::Navigate)
            fail(failure, ::handleFailure)
        }

    }

    private fun Navigate(idFragment: Int?) {
        if (idFragment != null && idFragment != 0) {
            view?.let { navigator.showFragment(it, idFragment) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadListFunction()
    }
}
