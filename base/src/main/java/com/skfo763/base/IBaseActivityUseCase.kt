package com.skfo763.base

import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver

interface IBaseActivityUseCase: LifecycleObserver {

    val res: Resources

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean

    fun <T: AppCompatActivity> startActivity(activityClass: Class<T>, params: Bundle = Bundle())

    fun finishActivity()

    fun <T: Service> startService (serviceClass: Class<T>, params: Bundle = Bundle())

    fun <T: Service> startForegroundService(serviceClass: Class<T>, params: Bundle = Bundle())

    fun <T: Service> stopService(serviceClass: Class<T>)

    fun <T> getIntentValue(key: String) : T?
}