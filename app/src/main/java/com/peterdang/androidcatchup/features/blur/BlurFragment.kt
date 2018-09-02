package com.peterdang.androidcatchup.features.blur


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.github.florent37.runtimepermission.RuntimePermission
import com.peterdang.androidcatchup.BR
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.blueprints.BaseFragment
import com.peterdang.androidcatchup.core.constant.Constants
import com.peterdang.androidcatchup.core.extensions.observe
import com.peterdang.androidcatchup.core.extensions.viewModel

/**
 * CHECK CASES:
 * 1. Permission
 *      a. all accepted: enable button select & click -> pick img
 *      b. denied: enable button select & click -> dialog request permission
 *      c. forever denied: disable all button & display snackbar (gotosetting)
 * 2. Select Img
 *      a. unselected Image: disable function buttons (GO, SEE FILE, CANCEL)
 *      b. selected Image: enable button GO
 * 3. Press GO button
 *      a. Press button: Display progress dialog & cancel button, cannot select Img,
 *                      . Complete job: display Go & See File button
 * 4. Press cancel
 *      a. press button: cancel jobs & display go button
 */
class BlurFragment : BaseFragment<BlurViewModel>() {
    private val TAG = "BlurFragment"

    override fun layoutId() = R.layout.fragment_blur

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        viewModel = viewModel(viewModelFactory) {
            observe(mWorkStatus, ::checkStatus)
        }
    }

    override fun setBindingVariable(binding: ViewDataBinding) {
        binding.setVariable(BR.viewmodel, viewModel)
        binding.setVariable(BR.view, this)
    }

    fun checkPermissionAndProcessPickImg() {
        var pickImgPermissions = listOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        RuntimePermission.askPermission(this)
                .request(pickImgPermissions)
                .onAccepted { pickImage() }
                .onDenied { result ->
                    AlertDialog.Builder(context)
                            .setMessage(R.string.we_need_your_permission)
                            .setPositiveButton(R.string.ok, { dialog, which -> result.askAgain() })
                            .setNegativeButton(R.string.no, { dialog, which -> dialog.dismiss() })
                            .show()
                    result.askAgain()
                }
                .onForeverDenied { result ->
                    notifyWithAction(R.string.we_need_your_permission, R.string.setting, { result.goToSettings() })
                }
                .ask()

    }

    fun seeFile() {
        viewModel.currentUri.get()?.let {
            val actionView = Intent(Intent.ACTION_VIEW, viewModel.currentUri.get())
            if (context != null && actionView.resolveActivity(context!!.packageManager) != null) {
                startActivity(actionView)
            }
        }
    }

    fun pickImage() {
        val chooseIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(chooseIntent, Constants.RequestCode.REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCode.REQUEST_CODE_IMAGE -> viewModel.handleImageRequestResult(data)
                else -> Log.d(TAG, "Unknown request code.")
            }
        } else {
            Log.e(TAG, String.format("Unexpected Result code %s", resultCode))
        }
    }
}
