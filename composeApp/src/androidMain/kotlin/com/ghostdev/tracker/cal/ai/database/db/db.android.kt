package com.ghostdev.tracker.cal.ai.database.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ghostdev.tracker.cal.ai.data.database.AppDatabase

fun provideDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val path = context.getDatabasePath("ratio.db").absolutePath
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        path
    )
}