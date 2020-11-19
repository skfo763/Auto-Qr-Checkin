package com.skfo763.gradle.lib

import com.skfo763.gradle.global.Versions
import com.skfo763.gradle.extension.*
import org.gradle.api.artifacts.dsl.DependencyHandler

object DataStoreLib {
    const val prefDataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
    const val protoDataStore = "androidx.datastore:datastore-core:${Versions.dataStore}"
    const val playServiceMaps = "com.google.android.gms:play-services-maps:${Versions.playServiceMap}"
    const val playServiceLocation = "com.google.android.gms:play-services-location:${Versions.playServiceMap}"
}

fun DependencyHandler.dataStoreDependency() {
    implementation(DataStoreLib.prefDataStore)
    implementation(DataStoreLib.protoDataStore)
}

fun DependencyHandler.googleMapsDependency() {
    implementation(DataStoreLib.playServiceMaps)
    implementation(DataStoreLib.playServiceLocation)
}