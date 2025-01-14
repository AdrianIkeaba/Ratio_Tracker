package com.ghostdev.tracker.cal.ai.data.datasource

import com.ghostdev.tracker.cal.ai.data.database.AppDatabase
import com.ghostdev.tracker.cal.ai.models.DailyNutrient
import com.ghostdev.tracker.cal.ai.models.Food
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.models.WaterIntake
import com.ghostdev.tracker.cal.ai.models.Weights
import kotlinx.datetime.LocalDate

class RatioDataSourceImpl(
    private val appDatabase: AppDatabase
) : RatioDataSource {

    // User operations
    override suspend fun getUser(): User? =
        appDatabase.userDao().getUser()

    override suspend fun saveUser(user: User) {
        appDatabase.userDao().saveUser(user)
    }

    // DailyNutrient operations
    override suspend fun getDailyNutrients(date: LocalDate): DailyNutrient? =
        appDatabase.dailyNutrientDao().getDailyNutrients(date)

    override suspend fun saveDailyNutrients(dailyNutrient: DailyNutrient) {
        appDatabase.dailyNutrientDao().saveDailyNutrients(dailyNutrient)
    }

    // WaterIntake operations
    override suspend fun getWaterIntake(date: LocalDate): WaterIntake? =
        appDatabase.waterIntakeDao().getWaterIntake(date)

    override suspend fun updateWaterIntake(waterIntake: WaterIntake) {
        appDatabase.waterIntakeDao().updateWaterIntake(waterIntake)
    }

    // Meal operations
    override suspend fun getMealsByDate(date: LocalDate): List<Meal> =
        appDatabase.mealDao().getMealsByDate(date)

    override suspend fun saveMeal(meal: Meal) : Long {
        return appDatabase.mealDao().saveMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        appDatabase.mealDao().deleteMeal(meal)
    }

    // Food operations
    override suspend fun getFoodsByMeal(mealId: Int): List<Food> =
        appDatabase.foodDao().getFoodsByMeal(mealId)

    override suspend fun saveFood(food: Food) {
        appDatabase.foodDao().saveFood(food)
    }

    override suspend fun deleteFood(food: Food) {
        appDatabase.foodDao().deleteFood(food)
    }


    //Weight operations
    override suspend fun addWeight(weight: Weights) {
        appDatabase.weightsDao().addWeight(weight)
    }

    override suspend fun getWeights(): List<Weights> =
        appDatabase.weightsDao().getWeights()

    override suspend fun deleteWeight(weight: Weights) {
        appDatabase.weightsDao().deleteWeight(weight)
    }
}