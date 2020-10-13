package com.skfo763.gradle.lib

import com.skfo763.gradle.global.TestVersions

object TestLib {
    const val jUnit = "junit:junit:${TestVersions.junit}"
    const val jUnitExt = "androidx.test.ext:junit:${TestVersions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${TestVersions.espressoCore}"
}