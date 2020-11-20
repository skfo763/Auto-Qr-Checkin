package com.skfo763.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CheckPointEntity::class], version = 1)
abstract class CheckPointDatabase: RoomDatabase() {
    abstract fun checkPointDao(): CheckPointDao
}