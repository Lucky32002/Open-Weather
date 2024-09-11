package uk.ac.tees.mad.d3896530

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices.getSettingsClient
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class LocationRepository(private val context: Context, private val activity: Activity) {

    private val settingsClient = getSettingsClient(context)
    private val locationRequest = LocationRequest.create()

    fun checkGpsSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(false)
            .setNeedBle(false)

        settingsClient.checkLocationSettings(builder.build())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.locationSettingsStates?.let {
                        Log.e("LocationSettings", it.toString())
                        // TODO: Handle successful location settings check
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("LocationSettings", "Failed to check location settings: ${exception.message}")
                if (exception is ResolvableApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Log.d(
                                "LocationSettings",
                                "Attempting to resolve location settings issue."
                            )
                            try {
                                exception.startResolutionForResult(activity, 0)
                            } catch (sendEx: IntentSender.SendIntentException) {
                                Log.d("LocationSettings", "Failed to send resolution request.")
                            }
                        }

                        else -> {
                            // Handle other status codes if needed
                        }
                    }
                }
            }
    }

    val gpsStatus = flow {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        while (currentCoroutineContext().isActive) {
            emit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            delay(3000)
        }
    }
}
