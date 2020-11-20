package com.skfo763.component.checkmap

data class NaverMapMarker(
    val checkPointIdx: Int,
    val latitude: Float,
    val longitude: Float,
    val address: String,
    val checkInTimeString: String
)