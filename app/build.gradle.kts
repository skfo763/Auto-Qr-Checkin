import com.skfo763.gradle.lib.*

plugins {
    `android-application`
    `signing-config`
    `load-local-properties-app`
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    defaultConfig {
        applicationId = com.skfo763.gradle.global.AppVersion.applicationId
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }

    buildTypes {
        getByName("debug") {
            firebaseCrashlytics {
                mappingFileUploadEnabled = false
            }
        }
    }
}

dependencies {
    implementation(project(":util"))
    implementation(project(":base"))
    implementation(project(":component"))
    implementation(project(":repository"))
    implementation(project(":storage"))
    implementation(project(":remote"))

    kotlinDependency()
    rxJavaDependency()
    androidCoreKtxDependency()
    daggerHiltDependency()
    lifecycleDependency()
    androidXUiDependency()
    navigationDependency()
    firebaseDependency()
    networkDependency()
    naverMapDependency()
    roomDependency()
}