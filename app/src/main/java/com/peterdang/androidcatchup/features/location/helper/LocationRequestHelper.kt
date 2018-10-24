package com.peterdang.androidcatchup.features.location.helper

import android.preference.PreferenceManager
import com.peterdang.androidcatchup.MyApplication
import javax.inject.Inject


class LocationRequestHelper
@Inject constructor(val context: MyApplication){
    val KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested"

    fun setRequesting(value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply()
    }

    fun getRequesting(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false)
    }
}