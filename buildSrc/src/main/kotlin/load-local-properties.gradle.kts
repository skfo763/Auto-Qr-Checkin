import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    defaultConfig {
        val adMobKey = gradleLocalProperties(rootDir).getProperty("admob.app.key")
        val checkInUrlNaver = gradleLocalProperties(rootDir).getProperty("checkin.url.naver")
        val checkInUrlKakao = gradleLocalProperties(rootDir).getProperty("checkin.url.kakao")
        manifestPlaceholders = mapOf("adMobAppKey" to adMobKey)
        buildConfigField("String", "AD_MOB_KEY", "\"$adMobKey\"")
        buildConfigField("String", "QR_CHECKIN_URL_NAVER", "\"$checkInUrlNaver\"")
        buildConfigField("String", "QR_CHECKIN_URL_KAKAO", "\"$checkInUrlKakao\"")
    }

    buildTypes {
        getByName("release") {
            val bannerAdUnitKey = gradleLocalProperties(rootDir).getProperty("admob.ads.unit.banner.release")
            val fullAdUnitKey = gradleLocalProperties(rootDir).getProperty("admob.ads.unit.full.release")
            buildConfigField("String", "AD_MOB_UNIT_BANNER", "\"$bannerAdUnitKey\"")
            buildConfigField("String", "AD_MOB_UNIT_FULL", "\"$fullAdUnitKey\"")
        }

        getByName("debug") {
            val bannerAdUnitKey = gradleLocalProperties(rootDir).getProperty("admob.ads.unit.banner.debug")
            val fullAdUnitKey = gradleLocalProperties(rootDir).getProperty("admob.ads.unit.full.debug")
            buildConfigField("String", "AD_MOB_UNIT_BANNER", "\"$bannerAdUnitKey\"")
            buildConfigField("String", "AD_MOB_UNIT_FULL", "\"$fullAdUnitKey\"")
        }
    }
}

