package com.jsontextfield.viable.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.FileOutputStream

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<ViaRailRoomDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("viarail.db")
    if (!dbFile.exists()) {
        context.assets.open("via.db").copyTo(FileOutputStream(dbFile))
    }
    return Room.databaseBuilder<ViaRailRoomDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}