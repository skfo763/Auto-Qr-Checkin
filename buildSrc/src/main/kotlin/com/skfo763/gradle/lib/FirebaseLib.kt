package com.skfo763.gradle.lib

import com.skfo763.gradle.global.Versions
import com.skfo763.gradle.extension.*
import org.gradle.kotlin.dsl.DependencyHandlerScope

object FirebaseLib {
    const val firebaseAds = "com.google.firebase:firebase-ads:${Versions.firebaseAds}"
    const val googleAds = "com.google.android.gms:play-services-ads:${Versions.firebaseAds}"
    const val analytics = "com.google.firebase:firebase-analytics-ktx:${Versions.analytics}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx:${Versions.crashlytics}"
    const val realtimeDB = "com.google.firebase:firebase-database-ktx:${Versions.realtimeDB}"
    const val fbCoroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1"
}

fun DependencyHandlerScope.firebaseDependency() {
    implementation(FirebaseLib.firebaseAds)
    implementation(FirebaseLib.googleAds)
    implementation(FirebaseLib.crashlytics)
    implementation(FirebaseLib.analytics)
    implementation(FirebaseLib.realtimeDB)
}