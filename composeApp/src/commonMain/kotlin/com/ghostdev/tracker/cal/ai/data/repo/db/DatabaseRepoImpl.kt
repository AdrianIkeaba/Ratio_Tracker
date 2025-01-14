package com.ghostdev.tracker.cal.ai.data.repo.db

import com.ghostdev.tracker.cal.ai.data.datasource.RatioDataSource
import com.ghostdev.tracker.cal.ai.models.DailyNutrient
import com.ghostdev.tracker.cal.ai.models.Food
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.models.WaterIntake
import com.ghostdev.tracker.cal.ai.models.Weights
import kotlinx.datetime.LocalDate

class DatabaseRepoImpl(
    private val dataSource: RatioDataSource
) : DatabaseRepo {

    // User operations
    override suspend fun getUser(): User? = dataSource.getUser()

    override suspend fun getUserName(): String {
        return dataSource.getUser()!!.name
    }

    override suspend fun saveUser(user: User) {
        dataSource.saveUser(user)
    }

    // DailyNutrient operations
    override suspend fun getDailyNutrients(date: LocalDate): DailyNutrient? =
        dataSource.getDailyNutrients(date)

    override suspend fun saveDailyNutrients(dailyNutrient: DailyNutrient) {
        dataSource.saveDailyNutrients(dailyNutrient)
    }

    // WaterIntake operations
    override suspend fun getWaterIntake(date: LocalDate): WaterIntake? =
        dataSource.getWaterIntake(date)

    override suspend fun updateWaterIntake(waterIntake: WaterIntake) {
        dataSource.updateWaterIntake(waterIntake)
    }


    // Meal operations
    override suspend fun getMealsByDate(date: LocalDate): List<Meal> =
        dataSource.getMealsByDate(date)

    override suspend fun saveMeal(meal: Meal) : Long {
        return dataSource.saveMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        dataSource.deleteMeal(meal)
    }


    // Food operations
    override suspend fun getFoodsByMeal(mealId: Int): List<Food> =
        dataSource.getFoodsByMeal(mealId)

    override suspend fun saveFood(food: Food) {
        dataSource.saveFood(food)
    }

    override suspend fun deleteFood(food: Food) {
        dataSource.deleteFood(food)
    }

    //Weight operations
    override suspend fun addWeight(weight: Weights) {
        dataSource.addWeight(weight)
    }

    override suspend fun getWeights(): List<Weights> =
        dataSource.getWeights()


    override suspend fun deleteWeight(weight: Weights) {
        dataSource.deleteWeight(weight)
    }
}