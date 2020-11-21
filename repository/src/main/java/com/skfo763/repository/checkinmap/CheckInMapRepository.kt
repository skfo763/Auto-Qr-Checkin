package com.skfo763.repository.checkinmap

import android.location.Location
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import com.skfo763.storage.room.CheckPointAddress
import com.skfo763.storage.room.CheckPointEntity
import kotlinx.coroutines.flow.Flow

interface CheckInMapRepository {

    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): CheckInAddress

    suspend fun getCheckPoint(address: CheckInAddress): List<CheckPoint>

    suspend fun getCheckPointWithOffset(offset: Int): List<CheckPoint>

    suspend fun getAllSavedAddress(): List<CheckInAddress>

    suspend fun saveCheckPoint(vararg checkPoint: CheckPoint)

    suspend fun getLastKnownLocation(): Location?

    suspend fun startTrackingLocation()

    suspend fun stopTrackingLocation()

}