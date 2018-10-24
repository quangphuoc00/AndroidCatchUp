package com.peterdang.androidcatchup.core.blueprints

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.peterdang.androidcatchup.BR
import com.peterdang.androidcatchup.MyApplication
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.di.ApplicationComponent
import com.peterdang.androidcatchup.core.exception.Failure
import com.peterdang.androidcatchup.core.extensions.appContext
import com.peterdang.androidcatchup.core.extensions.viewContainer
import com.peterdang.androidcatchup.features.home.usecases.NavigateUsecase
import javax.inject.Inject
import android.app.ProgressDialog



abstract class BaseFragment<VM : BaseViewModel> : Fragment(), LifecycleOwner {
    abstract fun layoutId(): Int

    private lateinit var dialog: ProgressDialog

    protected lateinit var viewModel: VM

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as MyApplication).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

//    internal fun showProgress() = progressStatus(View.VISIBLE)

//    internal fun hideProgress() = progressStatus(View.GONE)

    internal fun notify(@StringRes message: Int) =
            viewContainer.view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }

    internal fun notify(message: String) =
            viewContainer.view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        val snackBar = viewContainer.view?.let { Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE) }
        if (snackBar != null) {
            snackBar.setAction(actionText) { _ -> action.invoke() }
            snackBar.setActionTextColor(ContextCompat.getColor(appContext,
                    R.color.colorTextPrimary))
            snackBar.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        return inflater.inflate(layoutId(), container, false)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
                inflater, layoutId(), container, false)
        setBindingVariable(binding)
        dialog = ProgressDialog(context)
        dialog.setMessage(getString(R.string.loading))
        return binding.root
    }

    protected open fun setBindingVariable(binding: ViewDataBinding) {
        binding.setVariable(BR.viewmodel, viewModel)
    }

    protected fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is NavigateUsecase.FunctionNotImp -> notify(R.string.ActivityNotImp)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
//        movieList.invisible()
//        emptyView.visible()
//        hideProgress()
        notifyWithAction(message, R.string.action_refresh, ::onRefresh)
    }

    protected fun showProgressDialog(){
        dialog.show()
    }

    protected fun dimissProgressDialog(){
        dialog.dismiss()
    }

    protected fun onRefresh() {}
}