package com.skfo763.base

import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver

abstract class BaseActivityUseCase(private val activity: AppCompatActivity): IBaseActivityUseCase {

    override val res: Resources get() = activity.resources

    override fun <T: AppCompatActivity> startActivity(activityClass: Class<T>, params: Bundle) {
        val intent = Intent(activity, activityClass).apply {
            putExtras(params)
        }
        activity.startActivity(intent)
    }

    override fun finishActivity() {
        activity.finish()
    }

    override fun <T: Service> startService (serviceClass: Class<T>, params: Bundle) {
        activity.startService(Intent(activity, serviceClass))
    }

    override fun <T: Service> startForegroundService(serviceClass: Class<T>, params: Bundle) {
        val intent = Intent(activity, serviceClass).apply {
            putExtras(params)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent)
        } else {
            activity.startService(intent)
        }
    }

    override fun <T: Service> stopService(serviceClass: Class<T>) {
        activity.stopService(Intent(activity.applicationContext, serviceClass))
    }

    override fun <T> getIntentValue(key: String) : T? {
        return activity.intent.extras?.get(key) as? T
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return false
    }
}