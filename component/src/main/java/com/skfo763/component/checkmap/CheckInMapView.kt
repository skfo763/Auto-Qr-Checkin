package com.skfo763.component.checkmap

import android.content.Context
import android.graphics.PointF
import android.location.Location
import android.util.AttributeSet
import android.util.Log
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay

class CheckInMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attributeSet, defStyleAttr), LifecycleObserver, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener {

    var onCameraPositionChanged: ((LatLng) -> Unit)? = null

    var location: Location? = null
        set(value) {
            value?.let {
                setCurrentLocation(it)
            }
            field = value
        }

    var currentMarkers: List<Marker> = listOf()
        set(value) {
            field.forEach { it.map = null }
            getMapAsync { map ->
                value.forEach {
                    it.onClickListener = markerClickListener
                    it.map = map
                }
            }
            field = value
        }

    private val infoWindow = InfoWindow().apply {
        adapter = object: InfoWindow.DefaultTextAdapter(context) {
            override fun getText(window: InfoWindow): CharSequence = window.marker?.tag as? CharSequence ?: ""
        }
    }

    private val markerClickListener = Overlay.OnClickListener { overlay ->
        val marker = overlay as? Marker ?: return@OnClickListener true
        marker.infoWindow?.let {
            infoWindow.open(marker)
        } ?: run {
            infoWindow.close()
        }
        true
    }

    fun initializeMapSetting() {
        getMapAsync { map ->
            map.minZoom = 5.0
            map.maxZoom = 18.0
            map.setOnMapClickListener { _: PointF, _: LatLng -> infoWindow.close() }
            map.addOnCameraChangeListener(this)
            map.addOnCameraIdleListener(this)
        }
    }

    override fun onCameraChange(reason: Int, animated: Boolean) {
        if(BuildConfig.DEBUG) {
            Log.d("NaverMap", "카메라 변경 - reson: $reason, animated: $animated")
        }
    }

    override fun onCameraIdle() {
        getMapAsync {
            onCameraPositionChanged?.invoke(it.cameraPosition.target)
        }
    }

    private fun setCurrentLocation(location: Location) {
        getMapAsync {
            val latlng = LatLng(location.latitude, location.longitude)
            val cameraUpdate = CameraUpdate.scrollTo(latlng)
            cameraUpdate.animate(CameraAnimation.Easing)
            it.moveCamera(cameraUpdate)
            it.locationOverlay.position = latlng
            it.locationOverlay.isVisible = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        super.onDestroy()
    }
}