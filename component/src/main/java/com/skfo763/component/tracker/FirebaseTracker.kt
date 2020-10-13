package com.skfo763.component.tracker

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTracker @Inject constructor(val application: Application) {

    fun logAnalyticsEvent(event: String, param: Bundle) {
        FirebaseAnalytics.getInstance(application).logEvent(event, param)
    }

    fun sendUserProperties(key: String, value: String) {
        FirebaseAnalytics.getInstance(application).setUserProperty(key, value)
    }

}