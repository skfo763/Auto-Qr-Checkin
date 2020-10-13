package com.skfo763.repository.model

data class CheckInUrl(
    val url: String,
    val availableHost: List<String>,
    val availablePath: List<String>
)