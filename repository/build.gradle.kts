import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":util"))
    implementation(project(":storage"))
    implementation(project(":remote"))

    kotlinDependency()
    daggerHiltDependency()
}