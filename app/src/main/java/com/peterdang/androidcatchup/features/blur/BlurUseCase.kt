package com.peterdang.androidcatchup.features.blur

import android.net.Uri
import androidx.work.*
import com.peterdang.androidcatchup.core.constant.Constants
import com.peterdang.androidcatchup.features.blur.workers.BlurWorker
import com.peterdang.androidcatchup.features.blur.workers.CleanupWorker
import com.peterdang.androidcatchup.features.blur.workers.SaveImageToFileWorker
import javax.inject.Inject

/**
 * 4 steps:
 *  1. clean up temp file
 *  2. blur image
 *  3. save image
 *
 *  Credit at: https://codelabs.developers.google.com/codelabs/android-workmanager/#7
 */
class BlurUseCase
@Inject constructor() {
    var mWorkManager: WorkManager

    init {
        mWorkManager = WorkManager.getInstance()
    }

    fun run(blurLevel: Int, uri: Uri?) {
        // Add WorkRequest to Cleanup temporary images
        var continuation = mWorkManager
                .beginUniqueWork(Constants.BlurFeature.IMAGE_MANIPULATION_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest.from(CleanupWorker::class.java))

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequest.Builder(BlurWorker::class.java)

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri(uri))
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java)
                .addTag(Constants.BlurFeature.TAG_OUTPUT) // This adds the tag
                .build()
        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(mImageUri: Uri?): Data {
        val builder = Data.Builder()

        mImageUri?.let {
            builder.putString(Constants.BlurFeature.KEY_IMAGE_URI, mImageUri.toString())
        }
        return builder.build()
    }

    fun getStatusBlur() = mWorkManager.getStatusesByTag(Constants.BlurFeature.TAG_OUTPUT)

    fun cancelWorker() {
        mWorkManager.cancelUniqueWork(Constants.BlurFeature.IMAGE_MANIPULATION_WORK_NAME)
    }

    fun getOutputUri(): String? {
        val lstWorkStatus: List<WorkStatus>? = mWorkManager.getStatusesByTag(Constants.BlurFeature.TAG_OUTPUT).value

        lstWorkStatus?.let {
            val blurStatus: WorkStatus = lstWorkStatus.first()
            if (blurStatus.state.isFinished) {
                return blurStatus.outputData.getString(Constants.BlurFeature.KEY_IMAGE_URI)
            }
        }
        return null
    }
}
