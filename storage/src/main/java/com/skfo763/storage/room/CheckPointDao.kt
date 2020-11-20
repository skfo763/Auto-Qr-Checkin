package com.skfo763.storage.room

import androidx.room.*

@Dao
interface CheckPointDao {

    @Query("SELECT * FROM checkpoint WHERE addr_si_gun_gu == (:siGunGu) AND addr_large_si_do == (:largeSiDo)")
    suspend fun getCheckPointFromAddress(largeSiDo: String, siGunGu: String): List<CheckPointEntity>

    @Query("SELECT * FROM checkpoint WHERE checkpoint_idx > (:offset) LIMIT 10")
    suspend fun getCheckPointWithOffset(offset: Int): List<CheckPointEntity>

    @Query("SELECT DISTINCT country, addr_si_gun_gu, addr_large_si_do, addr_yup_myun_dong FROM checkpoint")
    suspend fun getAvailableAddressInfo(): List<CheckPointAddress>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckPoint(vararg checkpoint: CheckPointEntity)

    @Delete
    suspend fun deleteCheckPoint(vararg checkpoint: CheckPointEntity)

    @Query("DELETE FROM checkpoint WHERE checkpoint_idx == (:idx)")
    suspend fun deleteCheckPoint(vararg idx: Int)

}