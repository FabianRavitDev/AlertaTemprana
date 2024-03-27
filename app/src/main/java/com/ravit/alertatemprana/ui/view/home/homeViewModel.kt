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
import androidx.lifecycle.viewModelScope
import com.ravit.alertatemprana.network.NetworkManager
import com.ravit.alertatemprana.ui.model.LocationModel
import com.ravit.alertatemprana.ui.model.ServiceType
import com.ravit.alertatemprana.ui.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    interface LocationPermissionRequester {
        fun requestLocationPermissions()
        fun showLocationDisabledMessage()
    }

    var locationPermissionRequester: LocationPermissionRequester? = null
    lateinit var locat: LocationModel
    private var _location = mutableStateOf<Location?>(null)
    val location: Location? by _location
    private val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener = LocationListener { location ->
        Log.d("ViewModel", "Location: ${location.latitude} , ${location.longitude}")
        _location.value = location
        locat = LocationModel(
            id = "hola1",
            latitude = location.latitude,
            longitude = location.longitude,
            source_type = ServiceType.STANDARD
        )
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    private val _messageError = MutableStateFlow("")
    val messageError = _messageError.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    fun toggleDialog(show: Boolean) {
        _showDialog.value = show
    }
    fun toggleError(show: Boolean) {
        _error.value = show
    }

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

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

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        _isLoading.value = true
        if (isLocationPermissionGranted()) {
            locationListener = LocationListener { location ->
                Log.d("ViewModel", "Location: ${location.latitude} , ${location.longitude}")
                _location.value = location

                locat = LocationModel(
                    id = "hola1",
                    latitude = location.latitude,
                    longitude = location.longitude,
                    source_type = ServiceType.STANDARD
                )

                NetworkManager.sendLocation(locat, onSuccess = {
                    if (_isLoading.value) {
                        goToChat()
                        _isLoading.value = false
                    }
                }, onFailure = { error ->
                    stopLocationUpdates()
                    _error.value = true
                    _isLoading.value = false
                    Log.d("NetworkManager", "Error fuction: ${error}")
                    _messageError.value = error.toString()
                })
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } else {
            locationPermissionRequester?.requestLocationPermissions()
            _isLoading.value = false
        }
    }

    fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    fun goToChat() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToChat)
        }
    }
}