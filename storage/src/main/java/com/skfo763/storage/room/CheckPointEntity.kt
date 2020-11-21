package com.skfo763.storage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "checkpoint", indices = [Index(value = ["addr_large_si_do", "addr_si_gun_gu"])])
data class CheckPointEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "checkpoint_idx") val checkpointId: Int,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "addr_large_si_do") val largeSiDo: String,
    @ColumnInfo(name = "addr_si_gun_gu") val siGunGu: String,
    @ColumnInfo(name = "addr_yup_myun_dong") val yupMyunDong: String,
    @ColumnInfo(name = "checkin_time") val checkInTime: Long
)

data class CheckPointAddress(
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "addr_large_si_do") val largeSiDo: String,
    @ColumnInfo(name = "addr_si_gun_gu") val siGunGu: String,
    @ColumnInfo(name = "addr_yup_myun_dong") val yupMyunDong: String
)