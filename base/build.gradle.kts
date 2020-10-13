import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    id("dagger.hilt.android.plugin")
}

android {
    @Suppress("UnstableApiUsage")
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    kotlinDependency()
    daggerHiltDependency()
    navigationDependency()
    implementation(UiLib.appCompat)
}