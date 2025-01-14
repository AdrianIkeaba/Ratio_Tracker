package com.ghostdev.tracker.cal.ai.di

import com.ghostdev.tracker.cal.ai.data.database.AppDatabase
import com.ghostdev.tracker.cal.ai.data.database.getRoomDatabase
import com.ghostdev.tracker.cal.ai.database.db.provideDatabaseBuilder
import com.ghostdev.tracker.cal.ai.utilities.AndroidSystemService
import com.ghostdev.tracker.cal.ai.utilities.SystemService
import org.koin.dsl.module

actual fun platformModule() = module {
    single<AppDatabase> {
        val builder = provideDatabaseBuilder(get())
        getRoomDatabase(builder)
    }

    single<SystemService> { AndroidSystemService(get()) }
}