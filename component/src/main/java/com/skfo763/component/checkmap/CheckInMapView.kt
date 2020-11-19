package com.skfo763.component.checkmap

import android.content.Context
import android.location.Location
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView

class CheckInMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attributeSet, defStyleAttr), LifecycleObserver {


    var location: Location? = null
        set(value) {
            value?.let {
                setCurrentLocation(it)
            }
            field = value
        }

    fun initializeMapSetting() {
        getMapAsync {

        }
    }

    private fun setCurrentLocation(location: Location) {
        getMapAsync {
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
            cameraUpdate.animate(CameraAnimation.Easing)
            it.moveCamera(cameraUpdate)
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