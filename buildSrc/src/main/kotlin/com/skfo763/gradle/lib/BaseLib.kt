package com.skfo763.gradle.lib

import com.skfo763.gradle.global.Versions
import com.skfo763.gradle.extension.*
import org.gradle.api.artifacts.dsl.DependencyHandler

object AndroidCoreKtxLib {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
}

object KotlinLib {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
}

object DaggerHiltLib {
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltApp}"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltApp}"
    const val hiltCommon = "androidx.hilt:hilt-common:${Versions.hiltApp}"
}

fun DependencyHandler.androidCoreKtxDependency() {
    implementation(AndroidCoreKtxLib.coreKtx)
    implementation(AndroidCoreKtxLib.activityKtx)
    implementation(AndroidCoreKtxLib.fragmentKtx)
}

fun DependencyHandler.kotlinDependency() {
    implementation(KotlinLib.kotlin)
    implementation(KotlinLib.coroutine)
}

fun DependencyHandler.daggerHiltDependency() {
    kapt(DaggerHiltLib.hiltCompiler)
    kapt(DaggerHiltLib.daggerHiltCompiler)
    implementation(DaggerHiltLib.hilt)
    implementation(DaggerHiltLib.hiltViewModel)
    implementation(DaggerHiltLib.hiltCommon)
}