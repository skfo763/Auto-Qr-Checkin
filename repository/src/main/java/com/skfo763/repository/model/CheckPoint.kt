package com.skfo763.repository.model

data class CheckPoint(
    val checkPointIdx: Int,
    val latitude: Float,
    val longitude: Float,
    val address: CheckInAddress,
    val checkInTime: Long
)