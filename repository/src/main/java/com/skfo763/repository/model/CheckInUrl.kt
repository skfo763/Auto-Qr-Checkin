package com.skfo763.repository.model

import com.skfo763.remote.QrCheckInError

data class CheckInUrl(
    val url: String,
    val availableHost: List<String>,
    val availablePath: List<String>,
    val errorList: List<QrCheckInError>
)