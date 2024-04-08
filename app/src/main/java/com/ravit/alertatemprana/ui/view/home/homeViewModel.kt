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
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ravit.alertatemprana.network.NetworkManager
import com.ravit.alertatemprana.ui.model.LocationModel
import com.ravit.alertatemprana.ui.model.PositionModel
import com.ravit.alertatemprana.ui.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    interface LocationPermissionRequester {
        fun requestLocationPermissions()
        fun showLocationDisabledMessage()
    }

    var locationPermissionRequester: LocationPermissionRequester? = null
    lateinit var locat: PositionModel
    private var _location = mutableStateOf<Location?>(null)
    private val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener = LocationListener { location ->
        Log.d("NetworkManager", "LocationVM: ${location.latitude} , ${location.longitude}")
        _location.value = location
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    private val _messageError = MutableStateFlow("")
    val messageError = _messageError.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private var locationTimer: Timer? = null


    fun toggleDialog(show: Boolean) {
        _showDialog.value = show
    }
    fun toggleError(show: Boolean) {
        _error.value = show
    }

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _room_id = MutableStateFlow(0)
    val room_id = _room_id.asStateFlow()

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

    fun login() {
        _isLoading.value = true
        NetworkManager.loginLocation(onSuccess = {
            if (_isLoading.value) {
                sendFirstAlert()
                goToChat()
                _isLoading.value = false
            }
        }, onFailure = { error ->
            _error.value = true
            _isLoading.value = false
            Log.d("NetworkManager", "Error login: ${error}")
            _messageError.value = error.toString()
        })
    }

    fun sendFirstAlert() {
        _isLoading.value = true
        val data = LocationModel(description = "Send location", severity = "Media", status = "activa")
        NetworkManager.sendAlert(data,
            onSuccess = { locationModel ->
                _room_id.value = locationModel.room_id!! // TO DO
                Log.e("NetworkManager", "first alert: ${_room_id.value}")
//                startLocationUpdates()
            },
            onFailure = { error ->
                _error.value = true
                _messageError.value = error.toString()
                Log.e("NetworkManager", "Error al enviar first alerta: $error")
                // Lógica adicional para manejar el error
            })
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        _isLoading.value = true
        if (isLocationPermissionGranted()) {
            locationListener = LocationListener { location ->
                _location.value = location

                locat = PositionModel(
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                if (locationTimer == null) {
                    locationTimer = Timer()
                    locationTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            NetworkManager.sendLocation(locat,
                                onSuccess = {
                                    _isLoading.value = false
                                },
                                onFailure = { error ->
                                    _isLoading.value = false
                                    _error.value = true
                                    _messageError.value = error.toString()
                                    Log.e("NetworkManager", "Error en start location: $error")
                                })
                        }
                    }, 0, 10 * 1000)
                }
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
        _isLoading.value = true
        locationTimer?.cancel()
        locationTimer = null
        locationManager.removeUpdates(locationListener)

        NetworkManager.stopAlert(
            onSuccess = {
                Log.d("NetworkManager", "Alerta en stop correctamente")
                _isLoading.value = false
            },
            onFailure = { error ->
                _messageError.value = error.toString()
                Log.e("NetworkManager", "Error en stop location: $error")
                _isLoading.value = false
                _error.value = true
            })
    }

    fun goToChat() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToChat)
        }
    }
}