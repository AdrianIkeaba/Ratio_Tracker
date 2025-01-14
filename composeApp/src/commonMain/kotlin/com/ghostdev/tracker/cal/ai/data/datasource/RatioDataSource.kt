package com.ghostdev.tracker.cal.ai.data.datasource

import com.ghostdev.tracker.cal.ai.models.DailyNutrient
import com.ghostdev.tracker.cal.ai.models.Food
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.models.WaterIntake
import com.ghostdev.tracker.cal.ai.models.Weights
import kotlinx.datetime.LocalDate

interface RatioDataSource {
    // User operations
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)

    // DailyNutrient operations
    suspend fun getDailyNutrients(date: LocalDate): DailyNutrient?
    suspend fun saveDailyNutrients(dailyNutrient: DailyNutrient)

    // WaterIntake operations
    suspend fun getWaterIntake(date: LocalDate): WaterIntake?
    suspend fun updateWaterIntake(waterIntake: WaterIntake)

    // Meal operations
    suspend fun getMealsByDate(date: LocalDate): List<Meal>
    suspend fun saveMeal(meal: Meal) : Long
    suspend fun deleteMeal(meal: Meal)

    // Food operations
    suspend fun getFoodsByMeal(mealId: Int): List<Food>
    suspend fun saveFood(food: Food)
    suspend fun deleteFood(food: Food)

    //Weight operations
    suspend fun addWeight(weight: Weights)
    suspend fun getWeights(): List<Weights>
    suspend fun deleteWeight(weight: Weights)
}