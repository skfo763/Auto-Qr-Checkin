package com.skfo763.repository.model

import com.skfo763.remote.data.QrCheckInError

data class CheckInUrl(
    val url: String,
    val availableHost: List<String>,
    val availablePath: List<String>,
    val appLandingScheme: List<String>,
    val errorList: List<QrCheckInError>
)