import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    `load-local-properties-lib`
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")
}


dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(project(":app"))
    implementation(project(":util"))
    implementation(project(":base"))

    kotlinDependency()
    androidCoreKtxDependency()
    daggerHiltDependency()
    androidXUiDependency()
}