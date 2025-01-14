package com.ghostdev.tracker.cal.ai.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ghostdev.tracker.cal.ai.models.DailyNutrient
import com.ghostdev.tracker.cal.ai.models.Food
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.models.WaterIntake
import com.ghostdev.tracker.cal.ai.models.Weights
import kotlinx.datetime.LocalDate

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)
}

@Dao
interface DailyNutrientDao {
    @Query("SELECT * FROM daily_nutrient_table WHERE date = :date")
    suspend fun getDailyNutrients(date: LocalDate): DailyNutrient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyNutrients(dailyNutrient: DailyNutrient)
}

@Dao
interface WaterIntakeDao {
    @Query("SELECT * FROM water_intake_table WHERE date = :date")
    suspend fun getWaterIntake(date: LocalDate): WaterIntake?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWaterIntake(waterIntake: WaterIntake)
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_table WHERE date = :date")
    suspend fun getMealsByDate(date: LocalDate): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeal(meal: Meal) : Long

    @Delete
    suspend fun deleteMeal(meal: Meal)
}

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_table WHERE mealId = :mealId")
    suspend fun getFoodsByMeal(mealId: Int): List<Food>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)
}

@Dao
interface WeightsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeight(weights: Weights)

    @Query("SELECT * FROM weight_table ORDER BY id DESC")
    suspend fun getWeights(): List<Weights>

    @Delete
    suspend fun deleteWeight(weights: Weights)
}