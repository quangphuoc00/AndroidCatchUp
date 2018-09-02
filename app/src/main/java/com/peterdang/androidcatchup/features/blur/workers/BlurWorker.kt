package com.peterdang.androidcatchup.features.blur.workers

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.work.Data
import androidx.work.Worker
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.constant.Constants
import com.peterdang.androidcatchup.core.utils.ImageUtils
import javax.inject.Inject


/**
 * WorkManager:
 *   . Including: Worker, WorkRequest, OneTimeWorkRequest/ PeriodicWorkRequest, Constraints
 *
 * read more: https://expertise.jetruby.com/android-workmanager-the-future-is-coming-2b4bdd188050
 */
class BlurWorker : Worker() {
    private val TAG = "BlurWorker"

    @Inject
    lateinit var imageUtils: ImageUtils


    @SuppressLint("NewApi")
    @NonNull
    override fun doWork(): Worker.Result {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Toast.makeText(applicationContext, applicationContext.getString(R.string.require_min_android_version), Toast.LENGTH_SHORT).show()
            return Worker.Result.FAILURE
        }

        val resourceUri = inputData.getString(Constants.BlurFeature.KEY_IMAGE_URI)
        imageUtils = ImageUtils(applicationContext)
        try {

            if (resourceUri.isNullOrEmpty()) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = applicationContext.contentResolver
            // Create a bitmap
            val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)))

            // Blur the bitmap
            val output =
                    imageUtils.blurBitmap(picture)


            // Write bitmap to a temp file
            val outputUri = imageUtils.writeBitmapToFile(output)

            outputData = Data.Builder().putString(
                    Constants.BlurFeature.KEY_IMAGE_URI, outputUri.toString()).build()

            // If there were no errors, return SUCCESS
            return Worker.Result.SUCCESS
        } catch (throwable: Throwable) {

            // Technically WorkManager will return WorkerResult.FAILURE
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error applying blur", throwable)
            return Worker.Result.FAILURE
        }

    }
}