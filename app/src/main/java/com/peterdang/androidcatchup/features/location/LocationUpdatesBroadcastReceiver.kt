package com.peterdang.androidcatchup.features.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.peterdang.androidcatchup.features.location.helper.LocationResultHelper
import com.google.android.gms.location.LocationResult
import com.peterdang.androidcatchup.features.location.service.LocationUpdatesIntentService


/**
 * Receiver for handling location updates.
 *
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */
class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {
    companion object {
        internal val ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.backgroundlocationupdates.action" + ".PROCESS_UPDATES"
        private val TAG = LocationUpdatesBroadcastReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val action = intent.getAction()
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val locations = result.locations
                    val locationResultHelper = LocationResultHelper(
                            context)
                    locationResultHelper.mLocations = locations
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults()
                    // Show notification with the location data.
                    locationResultHelper.showNotification()
                    Log.i(TAG, locationResultHelper.getSavedLocationResult(context))
                }
            }
        }
    }

}