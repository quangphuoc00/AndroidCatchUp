package com.peterdang.androidcatchup.features.blur

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkStatus
import com.peterdang.androidcatchup.core.blueprints.BaseViewModel
import com.peterdang.androidcatchup.core.constant.Constants
import javax.inject.Inject

class BlurViewModel
@Inject constructor(private val blurUseCase: BlurUseCase) : BaseViewModel() {
    private val TAG = "BlurViewModel"

    var mWorkStatus: LiveData<List<WorkStatus>> = MutableLiveData()
    var blurLevel: ObservableField<Int> = ObservableField(1)

    var currentUri: ObservableField<Uri> = ObservableField()

    init {
        mWorkStatus = blurUseCase.getStatusBlur()
    }

    fun applyBlur() {
        isLoading.set(true)
        blurUseCase.run(blurLevel.get()!!, currentUri.get())
    }

    fun cancel() {
        isLoading.set(false)
        blurUseCase.cancelWorker()
    }

    override fun onCleared() {
        cancel()
    }


    fun checkStatus(lstWorkerStatus: List<WorkStatus>?) {
        // If there are no matching work statuses, do nothing
        if (lstWorkerStatus == null || lstWorkerStatus.isEmpty()) {
            return
        }

        val blurStatus = lstWorkerStatus.first()

        val finished = blurStatus.state.isFinished

        if (!finished) {
            isLoading.set(true)
        } else {
            isLoading.set(false)

            lstWorkerStatus.let {
                if (blurStatus.state.isFinished) {
                    currentUri.set(Uri.parse(blurStatus.outputData.getString(Constants.BlurFeature.KEY_IMAGE_URI)))
                }
            }
        }
    }

    fun handleImageRequestResult(data: Intent?) {
        var imageUri: Uri? = null
        if (data!!.clipData != null) {
            imageUri = data.clipData!!.getItemAt(0).uri
        } else if (data.data != null) {
            imageUri = data.data
        }

        if (imageUri == null) {
            Log.e(TAG, "Invalid input image Uri.")
            return
        }

        currentUri.set(imageUri)
    }
}
