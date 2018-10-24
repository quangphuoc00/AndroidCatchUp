package com.peterdang.androidcatchup.features.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.blueprints.BaseFragment
import com.peterdang.androidcatchup.core.extensions.fail
import com.peterdang.androidcatchup.core.extensions.observe
import com.peterdang.androidcatchup.core.extensions.viewModel
import com.peterdang.androidcatchup.core.navigation.MyNavigator
import javax.inject.Inject
import com.peterdang.androidcatchup.core.ui.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_home.*

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

    fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.addItemDecoration(ItemOffsetDecoration(context!!, R.dimen.space_item))
    }

    private fun Navigate(idFragment: Int?) {
        if (idFragment != null && idFragment != 0) {
            view?.let { navigator.showFragment(it, idFragment) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        viewModel.loadListFunction()
    }
}
