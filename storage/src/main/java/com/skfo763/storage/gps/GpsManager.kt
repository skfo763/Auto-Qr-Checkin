package com.skfo763.storage.gps

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class GpsManager(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationResult: LocationRequest? get() = LocationRequest.create().apply {
        interval = 600000
        fastestInterval = 60000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
        }

        override fun onLocationAvailability(locationAvailability : LocationAvailability?) {
            locationAvailability ?: return
        }
    }

    fun startLocationUpdate(looper: Looper? = Looper.getMainLooper()): Flow<Void>  = flow {
        fusedLocationClient.checkPermission().requestLocationUpdates(locationResult, locationCallback, looper).await()
    }

    fun stopLocationUpdate(): Flow<Void> = flow {
        fusedLocationClient.checkPermission().removeLocationUpdates(locationCallback).await()
    }

    suspend fun requestLastKnownLocation(): Location? {
        return fusedLocationClient.checkPermission().lastLocation.await()
    }

    private fun FusedLocationProviderClient.checkPermission(): FusedLocationProviderClient {
        if(context.isLocationPermissionGranted) {
            return this
        } else {
            throw GpsException.PermissionDeniedException()
        }
    }

}