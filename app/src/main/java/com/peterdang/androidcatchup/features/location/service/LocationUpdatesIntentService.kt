package com.peterdang.androidcatchup.features.location.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.peterdang.androidcatchup.features.location.helper.LocationResultHelper
import com.google.android.gms.location.LocationResult



/**
 * Handles incoming location updates and displays a notification with the location data.
 *
 * For apps targeting API level 25 ("Nougat") or lower, location updates may be requested
 * using [android.app.PendingIntent.getService] or
 * [android.app.PendingIntent.getBroadcast]. For apps targeting
 * API level O, only `getBroadcast` should be used.
 *
 * Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 * less frequently than the interval specified in the
 * [com.google.android.gms.location.LocationRequest] when the app is no longer in the
 * foreground.
 */
class LocationUpdatesIntentService : IntentService(TAG) {






    /**
     *
     *  1. It extracts the location result.
        2. It saves data about the location SharedPreferences.
            MainActivity.java implements an OnSharedPreferenceChangeListener,
            gets notified about the new location, and updates the UI.
        3. It creates a notification with information about the location received.
            That way, if location data is received while the app is not in the foreground, the notification shows the user the new data.
            This is not a persistent notification, and you can dismiss it if you like.
     */
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val locations = result.locations
                    val locationResultHelper = LocationResultHelper(this)
                    locationResultHelper.mLocations = locations
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults()
                    // Show notification with the location data.
                    locationResultHelper.showNotification()
                    Log.i(TAG, locationResultHelper.getSavedLocationResult(this))
                }
            }
        }
    }

    companion object {

        internal val ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.backgroundlocationupdates.action" + ".PROCESS_UPDATES"
        private val TAG = LocationUpdatesIntentService::class.java.simpleName
    }
}