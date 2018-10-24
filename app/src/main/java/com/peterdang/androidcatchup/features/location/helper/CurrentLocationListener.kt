package com.peterdang.androidcatchup.features.location.helper

import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.peterdang.androidcatchup.MyApplication
import javax.inject.Inject
import com.google.android.gms.common.api.GoogleApiClient


class CurrentLocationListener : GoogleApiClient.ConnectionCallbacks,
//        LiveData<Location>,
        GoogleApiClient.OnConnectionFailedListener {
    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TAG: String = "CurrentLocationListener"
//
//    private lateinit var mGoogleApiClient: GoogleApiClient
//
    @Inject
    constructor(context: MyApplication) {
//        buildGoogleApiClient(context)
    }
//
//    @Synchronized
//    private fun buildGoogleApiClient(appContext: Context) {
//        Log.d(TAG, "Build google api client")
//        mGoogleApiClient = GoogleApiClient.Builder(appContext)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build()
//
//        createLocationRequest();
//    }
//
//    private fun getPendingIntent(): PendingIntent? {
//        return null
//    }
//
//    override fun onActive() {
//        mGoogleApiClient.connect()
//    }
//
//    override fun onInactive() {
//        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, getPendingIntent())
//        }
//        mGoogleApiClient.disconnect()
//    }
//
//    override fun onConnected(connectionHint: Bundle?) {
//        Log.d(TAG, "connected to google api client")
//
//        val lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
//
//        if (lastLocation != null) {
//            setValue(lastLocation)
//        } else {
//            Log.e(TAG, "onConnected: last location value is NULL")
//        }
//
//        if (hasActiveObservers() && mGoogleApiClient.isConnected()) {
//            //            LocationRequest locationRequest = LocationHelper.createRequest();
//            val locationRequest = LocationRequest.create()
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this)
//        }
//    }
//
//    fun onLocationChanged(location: Location) {
//        Log.d(TAG, "Location changed received: $location")
//        setValue(location)
//    }
//
//    override fun onConnectionSuspended(cause: Int) {
//        Log.w(TAG, "On Connection suspended $cause")
//    }
//
//    override fun onConnectionFailed(result: ConnectionResult) {
//        Log.e(TAG, "mGoogleApiClient connection has failed $result")
//    }
//
//    /**
//     * Sets up the location request. Android has two location request settings:
//     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
//     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
//     * the AndroidManifest.xml.
//     *
//     *
//     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
//     * interval (5 seconds), the Fused Location Provider API returns location updates that are
//     * accurate to within a few feet.
//     *
//     *
//     * These settings are appropriate for mapping applications that show real-time location
//     * updates.
//     */
//    private fun createLocationRequest() {
//
//    }
}