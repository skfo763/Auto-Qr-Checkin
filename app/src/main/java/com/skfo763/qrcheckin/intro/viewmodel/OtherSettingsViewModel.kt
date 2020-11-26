package com.skfo763.qrcheckin.intro.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.skfo763.base.extension.logMessage
import com.skfo763.component.youtubeplayer.YouTubePlayerView
import com.skfo763.qrcheckin.intro.fragment.OtherSettingsFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class OtherSettingsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(), YouTubePlayerView.OnInitializedListener {

    private var youTubePlayer: YouTubePlayer? = null

    val maxVideoTime: Int get() = youTubePlayer?.durationMillis ?: Int.MAX_VALUE

    private val _videoTimeMillis = MutableLiveData<Int>()

    val videoTimeMillis: LiveData<Int> = _videoTimeMillis

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider,
        result: YouTubeInitializationResult
    ) {
        logMessage(result.name)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider,
        player: YouTubePlayer,
        wasRestored: Boolean
    ) {
        youTubePlayer = player
    }

    private var trackerDisposable: Disposable? = null

    fun startTracking() {
        trackerDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _videoTimeMillis.value = youTubePlayer?.currentTimeMillis
            }
    }

    fun setVideoTimeMillis(videoTimeMillis: Int) {
        _videoTimeMillis.value = videoTimeMillis
    }

    fun stopTracking() {
        if(trackerDisposable != null && !trackerDisposable!!.isDisposed) {
            trackerDisposable!!.dispose()
        }
    }
}