package com.jsontextfield.viable.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<ViaRailDatabase>
}