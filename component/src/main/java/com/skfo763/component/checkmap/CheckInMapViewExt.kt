package com.skfo763.component.checkmap

import android.location.Location
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.skfo763.base.theme.ThemeType
import com.skfo763.base.theme.getTheme
import com.skfo763.base.theme.isDarkTheme

object CheckInMapViewExt {

    @JvmStatic
    @BindingAdapter("location")
    fun CheckInMapView.setCheckInMapLocation(location: Location?) {
        location?.let {
            this.location = location
        }
    }

    @JvmStatic
    @BindingAdapter("markerLists")
    fun CheckInMapView.setMarkerList(markerLists: List<NaverMapMarker>? = null) {
        markerLists ?: return
        currentMarkers = markerLists.map {
            Marker().apply {
                position = LatLng(it.latitude, it.longitude)
                width = Marker.SIZE_AUTO
                height = Marker.SIZE_AUTO
                tag = it.checkInTimeString
            }
        }
    }

    /**
     * 세종특별자치시 주소를 구글 Reverse geocoding 에서 제공하지 않는 관계로,
     * 해당 좌표에 한해서는 "반드시" 네이버 reverse geocoding api를 사용해야 합니다.
     * Quota 문제로 다른 장소에 대해서는 구글 api를, 세종시에 관해서는 네이버 api를 사용합니다.
     */
    const val MAX_LATITUDE = 36.671218
    const val MIN_LATITUDE = 36.377666
    const val MAX_LONGITUDE = 127.520437
    const val MIN_LONGITUDE = 127.200359
    fun shouldUseNaverMapOnly(latitude: Double, longitude: Double): Boolean {
        return latitude in MIN_LATITUDE..MAX_LATITUDE && longitude in MIN_LONGITUDE..MAX_LONGITUDE
    }
}