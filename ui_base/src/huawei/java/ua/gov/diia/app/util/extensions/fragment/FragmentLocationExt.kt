package ua.gov.diia.app.util.extensions.fragment

import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.fragment.app.Fragment
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.gov.diia.core.models.select_location.Coordinate
import ua.gov.diia.ui_base.models.location.LocationWithStatus
import kotlin.coroutines.resume

suspend fun Fragment.requestCoordinate(
    updateInterval: Long = 1000,
    fastestUpdateInterval: Long = 500,
    providerPriority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY,
    maxRequestWaitTime: Long = 2000,
): Coordinate? = suspendCancellableCoroutine { cont ->
    val locationClient =
        LocationServices.getFusedLocationProviderClient(requireContext())

    val locationRequest = LocationRequest().apply {
        interval = updateInterval
        fastestInterval = fastestUpdateInterval
        priority = providerPriority
        maxWaitTime = maxRequestWaitTime
    }

    val callback = object : LocationCallback() {

        override fun onLocationResult(p0: LocationResult) {
            p0.locations.filterNotNull().also { locations ->
                //don't offer empty lists, offer only if we have some data
                val coordinate = if (locations.isNotEmpty()) {
                    val data = locations.lastOrNull()
                    if (data != null && !checkMock(data)) {
                        Coordinate(
                            latitude = data.latitude,
                            longitude = data.longitude,
                        )
                    } else {
                        null
                    }
                } else {
                    null
                }
                if (cont.isActive) {
                    locationClient.removeLocationUpdates(this)
                    cont.resume(coordinate)
                }
            }
        }

        override fun onLocationAvailability(la: LocationAvailability) {
            if (cont.isActive) cont.resume(null)
        }
    }

    locationClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())

    cont.invokeOnCancellation { locationClient.removeLocationUpdates(callback) }
}

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

                trySend(location)
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                super.onLocationAvailability(availability)
                if (System.currentTimeMillis() - launchTime < updateInterval / 2) { return }
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

private fun checkMock(location: Location): Boolean{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        location.isMock
    } else {
        location.isFromMockProvider
    }
}