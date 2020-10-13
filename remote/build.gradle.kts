import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        val checkInUrlNaver = gradleLocalProperties(rootDir).getProperty("checkin.url.naver")
        val checkInUrlKakao = gradleLocalProperties(rootDir).getProperty("checkin.url.kakao")
        buildConfigField("String", "QR_CHECKIN_URL_NAVER", "\"$checkInUrlNaver\"")
        buildConfigField("String", "QR_CHECKIN_URL_KAKAO", "\"$checkInUrlKakao\"")
    }
}

dependencies {
    implementation(project(":util"))

    kotlinDependency()
    daggerHiltDependency()
    firebaseDependency()
}