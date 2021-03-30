package com.noemi.android.hiltinjection.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArtEntity::class], version = 1, exportSchema = false)
abstract class ArtDataBase : RoomDatabase() {

    abstract fun artDao(): ArtDAO
}