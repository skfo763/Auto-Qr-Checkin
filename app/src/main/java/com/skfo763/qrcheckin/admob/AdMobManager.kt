package com.skfo763.qrcheckin.admob

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize
import com.skfo763.qrcheckin.BuildConfig
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class AdMobManager @Inject constructor(
    private val activity: Activity
) : LifecycleObserver {

    private var initialLayoutComplete = false
    private val random = Random()

    private val adView = AdView(activity)
    private val mInterstitialAd = InterstitialAd(activity)

    val shouldShowInterstitialAd: Boolean get() = mInterstitialAd.isLoaded && random.nextInt(4) == 0

    init {
        MobileAds.initialize(activity) { }
    }

    fun putAddToCustomContainer(container: FrameLayout) {
        container.addView(adView)
        container.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                adView.adUnitId = BuildConfig.AD_MOB_UNIT_BANNER
                adView.adSize = activity.getAdSize(container)
                adView.loadAd(AdRequest.Builder().build())
            }
        }
    }

    fun showInterstitialAd() {
        mInterstitialAd.show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun doOnCreate() {
        mInterstitialAd.apply {
            adUnitId = BuildConfig.AD_MOB_UNIT_FULL
            loadAd(AdRequest.Builder().build())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun doOnResume() {
        adView.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun doOnPause() {
        adView.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun doOnDestroy() {
        adView.destroy()
    }

    private fun Activity.getAdSize(adViewContainer: View): AdSize {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adViewContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }
}