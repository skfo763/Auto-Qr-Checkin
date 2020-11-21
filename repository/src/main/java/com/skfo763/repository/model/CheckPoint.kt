package com.skfo763.repository.model

data class CheckPoint(
    val latitude: Double,
    val longitude: Double,
    val address: CheckInAddress,
    val checkInTime: Long,
    val checkPointIdx: Int = 0
)