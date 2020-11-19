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
    implementation(project(":util"))
    implementation(project(":base"))

    kotlinDependency()
    androidCoreKtxDependency()
    daggerHiltDependency()
    androidXUiDependency()
    playCoreDependency()
    firebaseDependency()
    naverMapDependency()
}