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
        val naverMapClientId = gradleLocalProperties(rootDir).getProperty("maps.naver.clientid")
        val naverMapClientSecret = gradleLocalProperties(rootDir).getProperty("maps.naver.clientsecret")
        val localDBName = gradleLocalProperties(rootDir).getProperty("local.database.name")
        val localDBVersion = gradleLocalProperties(rootDir).getProperty("local.database.version").toIntOrNull() ?: 1

        manifestPlaceholders = mapOf("adMobAppKey" to adMobKey, "naverMapClientId" to naverMapClientId)
        buildConfigField("String", "AD_MOB_KEY", "\"$adMobKey\"")
        buildConfigField("String", "QR_CHECKIN_URL_NAVER", "\"$checkInUrlNaver\"")
        buildConfigField("String", "QR_CHECKIN_URL_KAKAO", "\"$checkInUrlKakao\"")
        buildConfigField("String", "NAVER_MAP_CLIENT_ID", "\"$naverMapClientId\"")
        buildConfigField("String", "NAVER_MAP_CLIENT_SECRET", "\"$naverMapClientSecret\"")
        buildConfigField("String", "LOCAL_DB_NAME", "\"$localDBName\"")
        buildConfigField("int", "LOCAL_DB_VERSION", "$localDBVersion")
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

