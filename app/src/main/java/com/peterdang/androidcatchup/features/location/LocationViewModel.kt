package com.peterdang.androidcatchup.features.location;

import androidx.databinding.ObservableField
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.peterdang.androidcatchup.features.location.usecase.LocationUseCase
import javax.inject.Inject
import com.peterdang.androidcatchup.core.blueprints.BaseViewModel
import com.google.android.gms.location.LocationServices



class LocationViewModel
@Inject constructor(private val locationUseCase: LocationUseCase) : BaseViewModel() {
    var result : ObservableField<String> = ObservableField()

    fun requestUpdate(){

    }

    fun removeUpdate(){

    }
}