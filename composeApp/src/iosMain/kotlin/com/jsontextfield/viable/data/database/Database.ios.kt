package com.jsontextfield.viable.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import viable.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class)
fun getDatabaseBuilder(): RoomDatabase.Builder<ViaRailRoomDatabase> {
    val dbFilePath = documentDirectory() + "/viarail.db"
    val fileManager = NSFileManager.defaultManager
    runBlocking {
        val file = Res.readBytes("files/via.db")
        fileManager.createFileAtPath(dbFilePath, file.toNSData(), null)
    }
    return Room.databaseBuilder<ViaRailRoomDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
