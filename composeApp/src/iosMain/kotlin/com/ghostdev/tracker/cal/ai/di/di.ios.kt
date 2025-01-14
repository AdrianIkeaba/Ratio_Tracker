package com.ghostdev.tracker.cal.ai.di

import com.ghostdev.tracker.cal.ai.data.database.AppDatabase
import com.ghostdev.tracker.cal.ai.data.database.getRoomDatabase
import com.ghostdev.tracker.cal.ai.database.provideDatabase
import com.ghostdev.tracker.cal.ai.utilities.IOSSystemService
import com.ghostdev.tracker.cal.ai.utilities.SystemService
import org.koin.dsl.module

actual fun platformModule() = module {
    single<AppDatabase> {
        val builder = provideDatabase()
        getRoomDatabase(builder)
    }
    single<SystemService> { IOSSystemService() }
}