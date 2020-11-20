import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig {
        val adMobKey = gradleLocalProperties(rootDir).getProperty("admob.app.key")
        val checkInUrlNaver = gradleLocalProperties(rootDir).getProperty("checkin.url.naver")
        val checkInUrlKakao = gradleLocalProperties(rootDir).getProperty("checkin.url.kakao")
        val naverMapUrl = gradleLocalProperties(rootDir).getProperty("maps.naver.url")
        val naverMapClientId = gradleLocalProperties(rootDir).getProperty("maps.naver.clientid")
        val naverMapClientSecret = gradleLocalProperties(rootDir).getProperty("maps.naver.clientsecret")

        buildConfigField("String", "AD_MOB_KEY", "\"$adMobKey\"")
        buildConfigField("String", "QR_CHECKIN_URL_NAVER", "\"$checkInUrlNaver\"")
        buildConfigField("String", "QR_CHECKIN_URL_KAKAO", "\"$checkInUrlKakao\"")
        buildConfigField("String", "NAVER_MAP_URL", "\"$naverMapUrl\"")
        buildConfigField("String", "NAVER_MAP_CLIENT_ID", "\"$naverMapClientId\"")
        buildConfigField("String", "NAVER_MAP_CLIENT_SECRET", "\"$naverMapClientSecret\"")
    }
}

