package com.peterdang.androidcatchup.features.location.helper

import android.annotation.TargetApi
import android.app.PendingIntent
import com.peterdang.androidcatchup.features.MainActivity
import android.content.Intent
import android.app.NotificationManager
import android.preference.PreferenceManager
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.core.app.TaskStackBuilder
import com.peterdang.androidcatchup.MyApplication
import com.peterdang.androidcatchup.R
import java.text.DateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class LocationResultHelper {

    val KEY_LOCATION_UPDATES_RESULT = "location-update-result"

    private val PRIMARY_CHANNEL = "default"
    private lateinit var mContext: Context

    var mLocations: List<Location> = ArrayList()

    private var mNotificationManager: NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    @Inject
    constructor(mContext: Context){
        this.mContext = mContext

        val channel : NotificationChannel = NotificationChannel(PRIMARY_CHANNEL,
                mContext.getString(R.string.default_channel),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLightColor(Color.GREEN);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager()!!.createNotificationChannel(channel);

    }


    /**
     * Returns the title for reporting about a list of [Location] objects.
     */
    private fun getLocationResultTitle(): String {
        val numLocationsReported = mContext.getResources().getQuantityString(
                R.plurals.num_locations_reported, mLocations.size, mLocations.size)
        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(Date())
    }

    private fun getLocationResultText(): String {
        if (mLocations.isEmpty()) {
            return mContext.getString(R.string.unknown_location)
        }
        val sb = StringBuilder()
        for (location in mLocations) {
            sb.append("(")
            sb.append(location.getLatitude())
            sb.append(", ")
            sb.append(location.getLongitude())
            sb.append(")")
            sb.append("\n")
        }
        return sb.toString()
    }

    /**
     * Saves location result as a string to [android.content.SharedPreferences].
     */
    fun saveResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply()
    }

    /**
     * Fetches location results from [android.content.SharedPreferences].
     */
    fun getSavedLocationResult(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "")
    }

    /**
     * Get the notification mNotificationManager.
     *
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private fun getNotificationManager(): NotificationManager? {
        if (mNotificationManager == null) {
            mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }
        return mNotificationManager
    }

    /**
     * Displays a notification with the location results.
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun showNotification() {
        val notificationIntent = Intent(mContext, MainActivity::class.java)

        // Construct a task stack.
        val stackBuilder = TaskStackBuilder.create(mContext)

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity::class.java)

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent)

        // Get a PendingIntent containing the entire back stack.
        val notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = Notification.Builder(mContext, PRIMARY_CHANNEL)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent)

        getNotificationManager()!!.notify(0, notificationBuilder.build())
    }
}