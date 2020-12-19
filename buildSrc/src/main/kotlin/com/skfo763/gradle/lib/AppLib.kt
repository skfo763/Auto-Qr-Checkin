package com.skfo763.gradle.lib

import com.skfo763.gradle.global.Versions
import com.skfo763.gradle.extension.*
import org.gradle.api.artifacts.dsl.DependencyHandler

object LifeCycleLib {
    const val lifeCycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifeCycleExt}"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifeCycleExt}"
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycleExt}"
}

object UiLib {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val tedPermission = "gun0912.ted:tedpermission:${Versions.tedPermission}"
    const val webKit = "androidx.webkit:webkit:${Versions.webKitVersion}"
    const val simpleRV = "com.dino.library:simplerecyclerview:${Versions.simpleRV}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
}

object NavigationLib {
    const val navFrag = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    const val navUi = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"
    const val navTesting = "androidx.navigation:navigation-testing:${Versions.navVersion}"
    const val navFragKtx = "androidx.navigation:navigation-fragment-ktx:2.3.0"
    const val navUiKtx = "androidx.navigation:navigation-ui-ktx:2.3.0"
}

object PlayCoreLib {
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
}

fun DependencyHandler.lifecycleDependency() {
    implementation(LifeCycleLib.lifeCycleExt)
    implementation(LifeCycleLib.lifecycleKtx)
    implementation(LifeCycleLib.liveDataKtx)
}

fun DependencyHandler.androidXUiDependency() {
    implementation(UiLib.appCompat)
    implementation(UiLib.constraintLayout)
    implementation(UiLib.material)
    implementation(UiLib.tedPermission)
    implementation(UiLib.webKit)
    implementation(UiLib.simpleRV)
    implementation(UiLib.glide)
}

fun DependencyHandler.navigationDependency() {
    implementation(NavigationLib.navFrag)
    implementation(NavigationLib.navFragKtx)
    implementation(NavigationLib.navUi)
    implementation(NavigationLib.navUiKtx)
    androidTestImplementation(NavigationLib.navTesting)
}

fun DependencyHandler.playCoreDependency() {
    implementation(PlayCoreLib.playCore)
}