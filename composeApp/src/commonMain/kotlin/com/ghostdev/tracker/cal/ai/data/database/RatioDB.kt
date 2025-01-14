@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.ghostdev.tracker.cal.ai.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ghostdev.tracker.cal.ai.models.DailyNutrient
import com.ghostdev.tracker.cal.ai.models.Food
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.models.WaterIntake
import com.ghostdev.tracker.cal.ai.models.Weights
import com.ghostdev.tracker.cal.ai.utilities.Converters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [User::class, Meal::class, Food::class, WaterIntake::class, DailyNutrient::class, Weights::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun foodDao(): FoodDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun dailyNutrientDao(): DailyNutrientDao
    abstract fun weightsDao(): WeightsDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}