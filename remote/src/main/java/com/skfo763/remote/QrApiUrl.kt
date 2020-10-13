package com.skfo763.remote

data class QrApiUrl(
    val url: String = BuildConfig.QR_CHECKIN_URL_NAVER,
    val availableHost: List<String> = listOf(),
    val availablePath: List<String> = listOf()
)