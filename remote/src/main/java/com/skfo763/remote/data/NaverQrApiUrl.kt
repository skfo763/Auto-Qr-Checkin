package com.skfo763.remote.data

import com.skfo763.remote.BuildConfig

data class NaverQrApiUrl(
    val url: String = BuildConfig.QR_CHECKIN_URL_NAVER,
    val availableHost: List<String> = listOf(),
    val availablePath: List<String> = listOf(),
    val appLandingScheme: List<String> = listOf(),
    val errorList: List<QrCheckInError> = listOf()
)

data class KakaoQrApiUrl(
    val intentUrl: String? = null,
    val browseUrl: String? = null
)

data class QrCheckInError(
    val url: String = "",
    val title: String = "",
    val message: String = "",
    val alternativeUrl: String? = null
)