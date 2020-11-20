package com.skfo763.component.checkmap

import android.location.Location
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.naver.maps.geometry.LatLng

object CheckInMapViewExt {

    @JvmStatic
    @BindingAdapter("location")
    fun CheckInMapView.setCheckInMapLocation(location: Location?) {
        location?.let {
            this.location = location
        }
    }

    @JvmStatic
    @BindingAdapter("onCameraPositionChanged")
    fun CheckInMapView.setOnCameraPositionChanged(onCameraChanged: ((LatLng) -> Unit)? = null) {
        this.onCameraPositionChanged = onCameraChanged
    }
}