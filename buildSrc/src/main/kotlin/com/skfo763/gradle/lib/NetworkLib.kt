package com.skfo763.gradle.lib

import org.gradle.api.artifacts.dsl.DependencyHandler
import com.skfo763.gradle.extension.*
import com.skfo763.gradle.global.Versions

object NetworkLib {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiCompiler = "com.squareup.moshi:moshi-codegen:${Versions.moshi}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
}

fun DependencyHandler.networkDependency() {
    implementation(NetworkLib.retrofit)
    implementation(NetworkLib.moshi)
    implementation(NetworkLib.moshiConverter)
    kapt(NetworkLib.moshiCompiler)
}