package com.skfo763.component.checkmap

import android.location.Location
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

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

    @JvmStatic
    @BindingAdapter("markerLists")
    fun CheckInMapView.setMarkerList(markerLists: List<NaverMapMarker>? = null) {
        markerLists ?: return
        currentMarkers = markerLists.map {
            Marker().apply {
                position = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                width = Marker.SIZE_AUTO
                height = Marker.SIZE_AUTO
                tag = it.checkInTimeString
            }
        }
    }
}