import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    `load-local-properties-lib`
    id("kotlin-parcelize")
}

android {
    @Suppress("UnstableApiUsage")
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    kotlinDependency()
    navigationDependency()
    firebaseDependency()
    implementation(UiLib.appCompat)
}