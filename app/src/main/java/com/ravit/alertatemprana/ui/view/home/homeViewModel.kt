package com.ravit.alertatemprana.ui.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    interface LocationPermissionRequester {
        fun requestLocationPermissions()
        fun showLocationDisabledMessage()
    }

    var locationPermissionRequester: LocationPermissionRequester? = null

    private var _location = mutableStateOf<Location?>(null)
    val location: Location? by _location
    private val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationListener = LocationListener { location ->
        Log.d("ViewModel", "Location: ${location.latitude} , ${location.longitude}")
        _location.value = location
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    getApplication(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

/*    @SuppressLint("MissingPermission")
    fun incrementCount() {
        if (isLocationPermissionGranted()) {
            if (isLocationPermissionGranted()) {
                val locationManager = getApplication<Application>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val provider = LocationManager.GPS_PROVIDER
                val locationListener = LocationListener { location ->
                    _location.value = location
                    Log.d("ViewModel", "Location: ${location.latitude} , ${location.longitude}")
                }
                locationManager.requestLocationUpdates(provider, 0L, 0f, locationListener)
            }
            _count.value++
        } else {
            locationPermissionRequester?.requestLocationPermissions()
        }
    }*/

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (isLocationPermissionGranted()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
        }
    }

    fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }
}