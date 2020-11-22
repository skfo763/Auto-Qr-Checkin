package com.skfo763.repository.checkinmap

import com.skfo763.base.logException
import com.skfo763.base.logMessage
import com.skfo763.remote.api.NaverMapApi
import com.skfo763.remote.data.ComplexRegion
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import com.skfo763.storage.gps.GpsManager
import com.skfo763.storage.room.CheckPointAddress
import com.skfo763.storage.room.CheckPointDao
import com.skfo763.storage.room.CheckPointEntity
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class CheckInMapRepositoryImpl @Inject constructor(
    private val naverMapApi: NaverMapApi,
    private val checkpointDB: CheckPointDao,
    private val gpsManager: GpsManager
) : CheckInMapRepository {

    override suspend fun getAddressFromLocation(latitude: Double, longitude: Double) = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                naverMapApi.getAddressByLocation("${longitude.toFloat()},${latitude.toFloat()}").results[0].regions.address
            } catch (e: Exception) {
                throw CheckInMapException.NoAddressInfoException(e.message)
            }
        }
    }

    override suspend fun getCheckPoint(address: CheckInAddress) = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                checkpointDB.getCheckPointFromAddress(address.largeSiDo, address.siGunGu).map {
                    CheckPoint(it.latitude, it.longitude, it.address, it.checkInTime, it.checkpointId)
                }
            } catch (e: Exception) {
                throw CheckInMapException.NoCheckPointException(e.message)
            }
        }
    }

    override suspend fun getCheckPointWithOffset(offset: Int) = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                checkpointDB.getCheckPointWithOffset(offset).map {
                    CheckPoint(
                        it.latitude,
                        it.longitude,
                        it.address,
                        it.checkInTime,
                        it.checkpointId
                    )
                }
            } catch (e: Exception) {
                throw CheckInMapException.NoCheckPointException(e.message)
            }
        }
    }

    override suspend fun getAllSavedAddress(): List<CheckInAddress> {
        return checkpointDB.getAvailableAddressInfo().map { it.address }
    }

    override suspend fun saveCheckPoint(vararg checkPoint: CheckPoint): Boolean {
        return try {
            checkpointDB.insertCheckPoint(*checkPoint.map { it.entity }.toTypedArray())
            true
        } catch (e: Exception) {
            logException(e)
            false
        }

    }

    override suspend fun getLastKnownLocation() = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                gpsManager.requestLastKnownLocation()
            } catch (e: Exception) {
                throw CheckInMapException.CannnotFoundLocationException(e.message)
            }
        }
    }

    override suspend fun deleteAllCheckPoint() {
        withContext(Dispatchers.IO) {
            try {
                checkpointDB.deleteAllCheckPoint()
            } catch (e: Exception) {
                logException(e)
            }
        }
    }

    override suspend fun startTrackingLocation() {
        withContext(Dispatchers.IO) {
            try {
                gpsManager.startLocationUpdate().collect {
                    logMessage("starting location updating success")
                }
            } catch (e: Exception) {
                logException(e)
            }
        }
    }

    override suspend fun stopTrackingLocation() {
        withContext(Dispatchers.IO) {
            try {
                gpsManager.stopLocationUpdate().collect {
                    logMessage("stopping location updating success")
                }
            } catch (e: Exception) {
                logException(e)
            }

        }
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