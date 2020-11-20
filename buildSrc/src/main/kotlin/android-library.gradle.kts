import com.skfo763.gradle.global.AppVersion
import com.skfo763.gradle.global.Sdk

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Sdk.compileSdk)

    defaultConfig {
        minSdkVersion(Sdk.minSdk)
        targetSdkVersion(Sdk.targetSdk)
        versionCode = AppVersion.versionCode
        versionName = AppVersion.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        this@kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
}