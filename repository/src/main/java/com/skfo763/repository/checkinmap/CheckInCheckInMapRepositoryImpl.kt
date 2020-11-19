package com.skfo763.repository.checkinmap

import com.skfo763.storage.gps.GpsManager
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class CheckInCheckInMapRepositoryImpl(
    val gpsManager: GpsManager
) : CheckInMapRepository {

}