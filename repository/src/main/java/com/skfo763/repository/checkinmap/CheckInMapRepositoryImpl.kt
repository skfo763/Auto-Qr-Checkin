package com.skfo763.repository.checkinmap

import android.location.Location
import com.skfo763.remote.api.NaverMapApi
import com.skfo763.remote.data.ComplexRegion
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import com.skfo763.storage.gps.GpsManager
import com.skfo763.storage.room.CheckPointAddress
import com.skfo763.storage.room.CheckPointDao
import com.skfo763.storage.room.CheckPointEntity
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class CheckInMapRepositoryImpl @Inject constructor(
    val naverMapApi: NaverMapApi,
    val checkpointDB: CheckPointDao,
    val gpsManager: GpsManager
) : CheckInMapRepository {

    override suspend fun getAddressFromLocation(
        latitude: Double,
        longitude: Double
    ): CheckInAddress {
        return naverMapApi.getAddressByLocation("${longitude.toFloat()},${latitude.toFloat()}").results[0].regions.address
    }

    override suspend fun getCheckPoint(address: CheckInAddress): List<CheckPoint> {
        return checkpointDB.getCheckPointFromAddress(address.largeSiDo, address.siGunGu).map {
            CheckPoint(it.checkpointId, it.latitude, it.longitude, it.address, it.checkInTime)
        }
    }

    override suspend fun getCheckPointWithOffset(offset: Int): List<CheckPoint> {
        return checkpointDB.getCheckPointWithOffset(offset).map {
            CheckPoint(it.checkpointId, it.latitude, it.longitude, it.address, it.checkInTime)
        }
    }

    override suspend fun getAllSavedAddress(): List<CheckInAddress> {
        return checkpointDB.getAvailableAddressInfo().map { it.address }
    }

    override suspend fun insertCheckPoint(vararg checkPoint: CheckPoint) {
        checkpointDB.insertCheckPoint(*checkPoint.map { it.entity }.toTypedArray())
    }

    @ExperimentalCoroutinesApi
    override fun getLastKnownLocation(): Flow<Location?> {
        return gpsManager.requestLastKnownLocation
    }

    private val ComplexRegion.address: CheckInAddress get() {
        return CheckInAddress(
            this.countryRegion.name,
            this.largeSiDoRegion.name,
            this.siGunGuRegion.name,
            this.yupMyunDongRegion.name
        )
    }

    private val CheckPointEntity.address: CheckInAddress get() {
        return CheckInAddress(
            this.country,
            this.largeSiDo,
            this.siGunGu,
            this.yupMyunDong
        )
    }

    private val CheckPointAddress.address: CheckInAddress get() {
        return CheckInAddress(
            this.country,
            this.largeSiDo,
            this.siGunGu,
            this.yupMyunDong
        )
    }

    private val CheckPoint.entity: CheckPointEntity get() {
        return CheckPointEntity(
            checkPointIdx,
            latitude,
            longitude,
            address.country,
            address.largeSiDo,
            address.siGunGu,
            address.yupMyunDong,
            checkInTime
        )
    }
}