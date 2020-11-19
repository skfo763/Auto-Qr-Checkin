import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    `load-local-properties-lib`
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
    firebaseDependency()
    implementation(UiLib.appCompat)
}