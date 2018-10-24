package com.peterdang.androidcatchup.features.location;

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.blueprints.BaseFragment
import com.peterdang.androidcatchup.core.extensions.viewModel
import android.app.AlertDialog
import android.util.Log
import com.github.florent37.runtimepermission.RuntimePermission
import com.peterdang.androidcatchup.core.constant.Constants
import com.google.android.gms.location.LocationRequest
import javax.inject.Inject
import com.google.android.gms.location.LocationServices
import androidx.appcompat.app.AppCompatActivity
import android.preference.PreferenceManager
import com.peterdang.androidcatchup.features.location.helper.LocationRequestHelper
import com.peterdang.androidcatchup.features.location.helper.LocationResultHelper
import kotlinx.android.synthetic.main.fragment_location.*
import android.app.PendingIntent
import android.view.View
import com.peterdang.androidcatchup.features.location.service.LocationUpdatesIntentService
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent


class LocationFragment : BaseFragment<LocationViewModel>(),
        SharedPreferences.OnSharedPreferenceChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private val TAG = "LocationFragment"

    override fun layoutId() = R.layout.fragment_location

    lateinit var mLocationRequest: LocationRequest

    @Inject
    lateinit var locationResultHelper: LocationResultHelper

    @Inject
    lateinit var locationRequestHelper: LocationRequestHelper

    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        viewModel = viewModel(viewModelFactory) {
            //            observe(navFragment, ::Navigate)
//            fail(failure, ::handleFailure)
        }

        requestPermissions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRequestUpdate.setOnClickListener({ view -> requestLocationUpdates(view) })
        btnRemoveUpdate.setOnClickListener({ view -> removeLocationUpdates(view) })
    }

    private fun requestPermissions() {
        var updateLocationPermissions = listOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)

        RuntimePermission.askPermission(this)
                .request(updateLocationPermissions)
                .onAccepted {
                    buildGoogleApiClient()
                }
                .onDenied { result ->
                    AlertDialog.Builder(context)
                            .setMessage(R.string.we_need_your_permission)
                            .setPositiveButton(R.string.ok, { dialog, which -> result.askAgain() })
                            .setNegativeButton(R.string.no, { dialog, which -> dialog.dismiss() })
                            .show()
                    result.askAgain()
                }
                .onForeverDenied { result ->
                    notifyWithAction(R.string.we_need_your_permission, R.string.setting, { result.goToSettings() })
                }
                .ask()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, s: String?) {
        if (s.equals(locationResultHelper.KEY_LOCATION_UPDATES_RESULT)) {
            tvLocationResult.setText(locationResultHelper.getSavedLocationResult(context!!));
        } else if (s.equals(locationRequestHelper.KEY_LOCATION_UPDATES_REQUESTED)) {
            updateButtonsState(locationRequestHelper.getRequesting());
        }
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(context)
                .registerOnSharedPreferenceChangeListener(this)
        mGoogleApiClient?.let {
            it.connect()
        }
    }

    override fun onResume() {
        super.onResume()
        updateButtonsState(locationRequestHelper.getRequesting())
        tvLocationResult.setText(locationResultHelper.getSavedLocationResult(context!!))
    }

    override fun onStop() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        mLocationRequest.interval = Constants.LocationConfig.LOCATION_UPDATE_INTERVAL

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.fastestInterval = Constants.LocationConfig.LOCATION_FASTEST_UPDATE_INTERVAL
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.maxWaitTime = Constants.LocationConfig.LOCATION_MAX_WAIT_TIME
    }

    /**
     * Builds [GoogleApiClient], enabling automatic lifecycle management using
     * [GoogleApiClient.Builder.enableAutoManage]. I.e., GoogleApiClient connects in
     * [AppCompatActivity.onStart], or if onStart() has already happened, it connects
     * immediately, and disconnects automatically in [AppCompatActivity.onStop].
     */
    private fun buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return
        }
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addConnectionCallbacks(this)
                .enableAutoManage(activity!!, this)
                .addApi(LocationServices.API)
                .build()
        createLocationRequest()
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private fun updateButtonsState(requestingLocationUpdates: Boolean) {
        if (requestingLocationUpdates) {
            btnRequestUpdate.setEnabled(false)
            btnRemoveUpdate.setEnabled(true)
        } else {
            btnRequestUpdate.setEnabled(true)
            btnRemoveUpdate.setEnabled(false)
        }
    }

    override fun onConnected(p0: Bundle?) {
        Log.i(TAG, "GoogleApiClient connected");
        dimissProgressDialog()
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onConnectionSuspended(i: Int) {
        val text = "Connection suspended"
        Log.w(TAG, "$text: Error code: $i")
        notify(R.string.connection_suspended)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        val text = "Exception while connecting to Google Play services";
        Log.w(TAG, text + ": " + connectionResult.getErrorMessage());
        notify(text)
    }

    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    fun requestLocationUpdates(view: View) {
        if (mGoogleApiClient!!.isConnected()) {
            try {
                Log.i(TAG, "Starting location updates")
                locationRequestHelper.setRequesting(true)
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, getPendingIntent())
            } catch (e: SecurityException) {
                locationRequestHelper.setRequesting(false)
                e.printStackTrace()
            }
        }else{
            showProgressDialog()
        }
    }

    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    fun removeLocationUpdates(view: View) {
        Log.i(TAG, "Removing location updates")
        locationRequestHelper.setRequesting(false)
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getPendingIntent())
    }
}
