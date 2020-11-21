package com.skfo763.component.checkmap

data class NaverMapMarker(
    val checkPointIdx: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val checkInTimeString: String
)