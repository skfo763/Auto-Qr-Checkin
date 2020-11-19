// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath(com.skfo763.gradle.global.GlobalDependencies.gradle)
        classpath(com.skfo763.gradle.global.GlobalDependencies.kotlin)
        classpath(com.skfo763.gradle.global.GlobalDependencies.hilt)
        classpath(com.skfo763.gradle.global.GlobalDependencies.googleService)
        classpath(com.skfo763.gradle.global.GlobalDependencies.firebaseCrashlytics)
    }
}

allprojects {
    repositories {
        google()
        jcenter()

        maven {
            setUrl("https://navercorp.bintray.com/maps")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}