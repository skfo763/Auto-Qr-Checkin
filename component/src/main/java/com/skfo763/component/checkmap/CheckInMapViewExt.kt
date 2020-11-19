package com.skfo763.component.checkmap

import android.location.Location
import androidx.databinding.BindingAdapter

object CheckInMapViewExt {

    @JvmStatic
    @BindingAdapter("location")
    fun CheckInMapView.setCheckInMapLocation(location: Location?) {
        location?.let {
            this.location = location
        }
    }
}