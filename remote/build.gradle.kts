import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    `load-local-properties-lib`
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":util"))

    kotlinDependency()
    daggerHiltDependency()
    firebaseDependency()
    networkDependency()
}