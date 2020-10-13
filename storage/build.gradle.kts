import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    id("dagger.hilt.android.plugin")
}

dependencies {
    kotlinDependency()
    daggerHiltDependency()
    dataStoreDependency()
}