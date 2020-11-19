package com.skfo763.storage.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM location ")
    fun getAll(): List<LocationEntity>



}