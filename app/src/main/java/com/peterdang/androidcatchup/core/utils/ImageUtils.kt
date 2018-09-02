package com.peterdang.androidcatchup.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject


class ImageUtils @Inject constructor(private val applicationContext: Context) {
    private val TAG = "ImageUtils"
    val OUTPUT_PATH = "blur_filter_outputs"

//    fun makeStatusNotification(message: String) {
//
//        // Make a channel if necessary
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel, but only on API 26+ because
//            // the NotificationChannel class is new and not in the support library
//            val name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME
//            val description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
//            channel.description = description
//
//            // Add the channel
//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            notificationManager?.createNotificationChannel(channel)
//        }
//
//        // Create the notification
//        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(Constants.NOTIFICATION_TITLE)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(LongArray(0))
//
//        // Show the notification
//        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build())
//    }

    /**
     * Blurs the given Bitmap image
     * @param bitmap Image to blur
     * @param applicationContext Application context
     * @return Blurred bitmap image
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @WorkerThread
    fun blurBitmap(@NonNull bitmap: Bitmap): Bitmap {

        var rsContext: RenderScript? = null
        try {

            // Create the output bitmap
            val output = Bitmap.createBitmap(
                    bitmap.width, bitmap.height, bitmap.config)

            // Blur the image
            rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
            val inAlloc = Allocation.createFromBitmap(rsContext, bitmap)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)
            val theIntrinsic = ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))
            theIntrinsic.setRadius(10f)
            theIntrinsic.setInput(inAlloc)
            theIntrinsic.forEach(outAlloc)
            outAlloc.copyTo(output)

            return output
        } finally {
            rsContext?.finish()
        }
    }

    /**
     * Writes bitmap to a temporary file and returns the Uri for the file
     * @param applicationContext Application context
     * @param bitmap Bitmap to write to temp file
     * @return Uri for temp file with bitmap
     * @throws FileNotFoundException Throws if bitmap file cannot be found
     */
    @Throws(FileNotFoundException::class)
    fun writeBitmapToFile(@NonNull bitmap: Bitmap): Uri {

        val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
        val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ignore: IOException) {
                }

            }
        }
        return Uri.fromFile(outputFile)
    }
}