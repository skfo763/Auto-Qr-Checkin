package com.skfo763.storage.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.skfo763.base.checkPermissionGranted
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.suspendCoroutine

class GpsManager(private val context: Context) {

    private val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationResult: LocationRequest? get() = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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

    private val Context.isLocationPermissionGranted: Boolean get() {
        return this.checkPermissionGranted(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun FusedLocationProviderClient.checkPermission(): FusedLocationProviderClient {
        if(context.isLocationPermissionGranted) {
            return this
        } else {
            throw GpsException.PermissionDeniedException()
        }
    }

}