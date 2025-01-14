package com.ghostdev.tracker.cal.ai.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val goal: String,
    val gender: String,
    val activityLevel: String,
    val heightFeet: Int,
    val heightInches: Int,
    val weight: Float,
    val weightUnit: String,
    val age: Int,
    val recommendedProteins: Int,
    val recommendedFats: Int,
    val recommendedCarbs: Int,
    val recommendedCalories: Int,
    val recommendedWaterIntake: Float
)

@Entity(tableName = "daily_nutrient_table")
data class DailyNutrient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalTime,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
    val calories: Float
)

@Entity(tableName = "water_intake_table")
data class WaterIntake(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: LocalDate,
    val currentIntake: Float,
    val lastUpdatedTime: String
)

@Entity(tableName = "meal_table")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val time: LocalTime,
    val name: String,
    val totalCalories: Float,
    val totalProteins: Float,
    val totalFats: Float,
    val totalCarbs: Float
)

@Entity(
    tableName = "food_table",
    foreignKeys = [
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mealId")]
)
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: Int,
    val name: String,
    val quantityInGrams: Float,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)

@Entity(tableName = "weight_table")
data class Weights(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val weight: Float
)