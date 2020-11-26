import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.skfo763.gradle.global.AppVersion
import com.skfo763.gradle.global.Sdk

plugins {
    id("com.android.application")
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
        setProperty("archivesBaseName", "${AppVersion.applicationId}-v$versionName($versionCode)")
        multiDexEnabled = true

        ndk {
            abiFilters("armeabi-v7a", "x86", "arm64-v8a", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
        }

        getByName("debug") {
            val localDBName = gradleLocalProperties(rootDir).getProperty("local.database.name")
            resValue("string", "DB_PASSWORD_$localDBName", "1027")

            isDebuggable = true
            applicationIdSuffix = ".debug"
            aaptOptions.cruncherEnabled = false
            splits.abi.isEnable = false
            splits.density.isEnable = false
        }
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