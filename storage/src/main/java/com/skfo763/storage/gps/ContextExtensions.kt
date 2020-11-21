package com.skfo763.storage.gps

import android.Manifest
import android.content.Context
import com.skfo763.base.checkPermissionGranted

val Context.isLocationPermissionGranted: Boolean get() {
    return this.checkPermissionGranted(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}