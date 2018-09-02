package com.peterdang.androidcatchup.features.blur.workers

import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import com.peterdang.androidcatchup.core.constant.Constants
import java.io.File


class CleanupWorker : Worker() {
    private val TAG = "CleanUpWorker"

    override fun doWork(): Result {
        try {
            val outputDirectory = File(applicationContext.filesDir,
                    Constants.BlurFeature.OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null && entries.size > 0) {
                    for (entry in entries) {
                        val name = entry.name
                        if (!TextUtils.isEmpty(name) && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(TAG, String.format("Deleted %s - %s",
                                    name, deleted))
                        }
                    }
                }
            }

            return Worker.Result.SUCCESS
        } catch (exception: Exception) {
            Log.e(TAG, "Error cleaning up", exception)
            return Worker.Result.FAILURE
        }

    }
}