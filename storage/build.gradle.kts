import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    `load-local-properties-lib`
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":util"))
    implementation(project(":base"))

    kotlinDependency()
    daggerHiltDependency()
    dataStoreDependency()
    googleMapsDependency()
    roomDependency()
}