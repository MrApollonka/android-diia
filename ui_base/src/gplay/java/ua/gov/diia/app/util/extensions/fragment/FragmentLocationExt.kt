package ua.gov.diia.app.util.extensions.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.gov.diia.core.models.select_location.Coordinate
import ua.gov.diia.ui_base.models.location.LocationWithStatus
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
suspend fun Fragment.requestCoordinate(
    maxRetries: Int = 3,
    delayMillis: Long = 500,
    updateInterval: Long = 100,
    fastestUpdateInterval: Long = 50,
    providerPriority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY,
    maxRequestWaitTime: Long = 100
): Coordinate? = coroutineScope {
    val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    repeat(maxRetries) {
        val coordinate = suspendCancellableCoroutine<Coordinate?> { cont ->
            val locationRequest = LocationRequest.create().apply {
                interval = updateInterval
                fastestInterval = fastestUpdateInterval
                priority = providerPriority
                maxWaitTime = maxRequestWaitTime
            }

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val data = result.locations.lastOrNull()?.takeIf { !checkMock(it) }
                    val coordinate = data?.let {
                        Coordinate(latitude = it.latitude, longitude = it.longitude)
                    }

                    if (cont.isActive) {
                        locationClient.removeLocationUpdates(this)
                        cont.resume(coordinate)
                    }
                }

                override fun onLocationAvailability(availability: LocationAvailability) {
                    if (cont.isActive) cont.resume(null)
                }
            }

            locationClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
            cont.invokeOnCancellation { locationClient.removeLocationUpdates(callback) }
        }

        if (coordinate != null) return@coroutineScope coordinate
        delay(delayMillis)
    }
    return@coroutineScope null
}


@SuppressLint("MissingPermission")
fun Fragment.requestLocationUpdates(
    updateInterval: Long = 10000,
    fastestUpdateInterval: Long = 3000,
): Flow<LocationWithStatus> {
    return callbackFlow {
        val launchTime = System.currentTimeMillis()
        val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val request = LocationRequest.create()
            .setInterval(updateInterval)
            .setFastestInterval(fastestUpdateInterval)
        var lastLocationStatus = LocationWithStatus.Status.NOT_AVAILABLE
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                val data = result.locations.lastOrNull()
                val location = if (data == null) {
                    LocationWithStatus(LocationWithStatus.Status.NOT_AVAILABLE, null)
                } else if (checkMock(data)) {
                    LocationWithStatus(LocationWithStatus.Status.MOCK, null)
                } else {
                    LocationWithStatus(
                        LocationWithStatus.Status.VALID,
                        Coordinate(data.latitude, data.longitude),
                        data.accuracy.toDouble()
                    )
                }

                val isMock =
                    lastLocationStatus == LocationWithStatus.Status.MOCK && location.status == LocationWithStatus.Status.MOCK
                val isNotAvailable =
                    lastLocationStatus == LocationWithStatus.Status.NOT_AVAILABLE && location.status == LocationWithStatus.Status.NOT_AVAILABLE
                if (!isMock && !isNotAvailable) {
                    lastLocationStatus = location.status
                    trySend(location)
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                super.onLocationAvailability(availability)
                if (System.currentTimeMillis() - launchTime < updateInterval / 2) {
                    return
                }
                if (!availability.isLocationAvailable) {
                    trySend(
                        LocationWithStatus(LocationWithStatus.Status.NOT_AVAILABLE, null)
                    )
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose { locationClient.removeLocationUpdates(locationCallback) }
    }
}

@SuppressLint("MissingPermission")
suspend fun Fragment.requestLastKnownLocation(): Coordinate? =
    suspendCancellableCoroutine { cont ->
        val locationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        locationClient.lastLocation.apply {
            addOnSuccessListener { location ->
                val coordinate = if (location != null && !checkMock(location)) {
                    Coordinate(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )
                } else {
                    null
                }
                if (cont.isActive) cont.resume(coordinate)
            }

            addOnCanceledListener {
                if (cont.isActive) cont.resume(null)
            }
            addOnFailureListener {
                if (cont.isActive) cont.resume(null)
            }
        }
    }

private fun checkMock(location: Location): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        location.isMock
    } else {
        location.isFromMockProvider
    }
}