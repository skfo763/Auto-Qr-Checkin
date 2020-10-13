package com.skfo763.auto_qr_checkin.admob

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize
import com.skfo763.auto_qr_checkin.BuildConfig
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class AdMobManager @Inject constructor(
    private val activity: Activity
) : LifecycleObserver {

    private var initialLayoutComplete = false
    private var isInterstitialAdShown = false
    private val random = Random()

    private val adView = AdView(activity)
    private val mInterstitialAd = InterstitialAd(activity)

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

    fun showInterstitialAd(doOnFailed: () -> Unit) {
        if(mInterstitialAd.isLoaded && !isInterstitialAdShown && random.nextInt(4) == 0) {
            mInterstitialAd.show()
            isInterstitialAdShown = true
        } else {
            isInterstitialAdShown = false
            doOnFailed()
        }
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