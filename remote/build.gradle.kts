import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.skfo763.gradle.lib.*

plugins {
    `android-library`
    `load-local-properties-lib`
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        val checkInUrlNaver = gradleLocalProperties(rootDir).getProperty("checkin.url.naver")
        val checkInUrlKakao = gradleLocalProperties(rootDir).getProperty("checkin.url.kakao")
        val naverMapClientId = gradleLocalProperties(rootDir).getProperty("maps.naver.clientid")
        val naverMapClientSecret = gradleLocalProperties(rootDir).getProperty("maps.naver.clientsecret")
        buildConfigField("String", "QR_CHECKIN_URL_NAVER", "\"$checkInUrlNaver\"")
        buildConfigField("String", "QR_CHECKIN_URL_KAKAO", "\"$checkInUrlKakao\"")
        buildConfigField("String", "NAVER_MAP_CLIENT_ID", "\"$naverMapClientId\"")
        buildConfigField("String", "NAVER_MAP_CLIENT_SECRET", "\"$naverMapClientSecret\"")
    }
}

dependencies {
    implementation(project(":util"))

    kotlinDependency()
    daggerHiltDependency()
    firebaseDependency()
    networkDependency()
}