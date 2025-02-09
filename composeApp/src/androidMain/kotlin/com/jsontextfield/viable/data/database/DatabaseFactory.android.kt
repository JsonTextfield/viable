package com.jsontextfield.viable.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<ViaRailDatabase> {
        return Room.databaseBuilder(
            context,
            ViaRailDatabase::class.java,
            "viarail.db"
        ).createFromAsset("via.db")
    }
}