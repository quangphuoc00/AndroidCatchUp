package com.peterdang.androidcatchup.features.blur.workers

import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import com.peterdang.androidcatchup.core.constant.Constants
import java.text.SimpleDateFormat
import java.util.*

class SaveImageToFileWorker : Worker() {
    private val TAG = "SaveImageToFileWorker"

    private val TITLE = "Blurred Image"
    private val DATE_FORMATTER = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())

    override fun doWork(): Result {
        val resolver = applicationContext.contentResolver
        try {
            val resourceUri = inputData.getString(Constants.BlurFeature.KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, TITLE, DATE_FORMATTER.format(Date()))

            if (imageUrl.isNullOrEmpty()) {
                Log.e(TAG, "Writing to MediaStore failed")
                return Worker.Result.FAILURE
            }

            val output = Data.Builder()
                    .putString(Constants.BlurFeature.KEY_IMAGE_URI, imageUrl)
                    .build()
            outputData = output

            return Worker.Result.SUCCESS
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to save image to Gallery", exception)
            return Worker.Result.FAILURE
        }
    }
}