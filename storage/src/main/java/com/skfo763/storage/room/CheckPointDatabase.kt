package com.skfo763.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skfo763.storage.BuildConfig

@Database(entities = [CheckPointEntity::class], version = BuildConfig.LOCAL_DB_VERSION)
abstract class CheckPointDatabase: RoomDatabase() {
    abstract fun checkPointDao(): CheckPointDao
}