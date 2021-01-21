// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version by extra("1.4.20")
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath(com.skfo763.gradle.global.GlobalDependencies.gradle)
        classpath(com.skfo763.gradle.global.GlobalDependencies.hilt)
        classpath(com.skfo763.gradle.global.GlobalDependencies.googleService)
        classpath(com.skfo763.gradle.global.GlobalDependencies.firebaseCrashlytics)
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()

        maven {
            setUrl("https://naver.jfrog.io/artifactory/maven/")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}