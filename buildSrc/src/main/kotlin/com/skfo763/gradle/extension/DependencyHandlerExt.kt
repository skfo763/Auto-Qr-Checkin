package com.skfo763.gradle.extension

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotation: String) {
    add("implementation", dependencyNotation)
}

fun DependencyHandler.kapt(name: String) {
    add("kapt", name)
}

fun DependencyHandler.compileOnly(name: String) {
    add("compileOnly", name)
}

fun DependencyHandler.debugImplementation(name: String) {
    add("debugImplementation", name)
}

fun DependencyHandler.testImplementation(name: String) {
    add("testImplementation", name)
}

fun DependencyHandler.androidTestImplementation(name: String) {
    add("androidTestImplementation", name)
}
