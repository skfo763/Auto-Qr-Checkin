package com.skfo763.gradle.lib

import com.skfo763.gradle.global.Versions
import com.skfo763.gradle.extension.*
import org.gradle.api.artifacts.dsl.DependencyHandler

object DataStoreLib {
    const val prefDataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
    const val protoDataStore = "androidx.datastore:datastore-core:${Versions.dataStore}"
}

fun DependencyHandler.dataStoreDependency() {
    implementation(DataStoreLib.prefDataStore)
    implementation(DataStoreLib.protoDataStore)
}