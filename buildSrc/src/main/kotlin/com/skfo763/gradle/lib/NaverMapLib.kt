package com.skfo763.gradle.lib

import org.gradle.api.artifacts.dsl.DependencyHandler
import com.skfo763.gradle.extension.*
import com.skfo763.gradle.global.Versions

object NaverMapLib {
    const val naverMapSdk = "com.naver.maps:map-sdk:${Versions.naverMap}"
}

fun DependencyHandler.naverMapDependency() {
    implementation(NaverMapLib.naverMapSdk)
}