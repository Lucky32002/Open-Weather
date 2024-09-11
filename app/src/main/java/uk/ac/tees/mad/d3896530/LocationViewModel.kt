package uk.ac.tees.mad.d3896530

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import uk.ac.tees.mad.d3896530.models.Coord
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    val locationState = mutableStateOf(Coord(0.0, 0.0))
    private val locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    val locationFlow = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    trySend(location)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }.shareIn(
        viewModelScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    updateLocation(location)
                }
            }
        }
        initiateLocationUpdates()
    }

    fun initiateLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Consider requesting permissions here
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun updateLocation(location: Location?) {
        location?.let {
            locationState.value = Coord(
                lat = it.latitude,
                lon = it.longitude
            )
        }
    }

    companion object {
        private const val ONE_MINUTE: Long = 60_000L
        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, ONE_MINUTE)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(ONE_MINUTE / 4)
            .setMaxUpdateDelayMillis(ONE_MINUTE / 2)
            .build()
    }
}
