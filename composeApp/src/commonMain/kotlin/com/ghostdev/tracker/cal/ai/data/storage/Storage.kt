package com.ghostdev.tracker.cal.ai.data.storage

import com.ghostdev.tracker.cal.ai.models.Recipe
import com.ghostdev.tracker.cal.ai.models.RecipeResponse
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.OnboardingUiState
import com.ghostdev.tracker.cal.ai.presentation.home.diary.meals.FoodUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface Storage {
    suspend fun getUserName(): Flow<String>
    suspend fun updateUserName(name: String)
    suspend fun getOnboardingState(): Flow<OnboardingUiState>
    suspend fun updateOnboardingName(name: String)
    suspend fun updateOnboardingGoal(goal: String)
    suspend fun updateOnboardingGender(gender: String)
    suspend fun updateOnboardingActivity(activity: String)
    suspend fun updateOnboardingHeightFeet(heightFeet: Int)
    suspend fun updateOnboardingHeightInches(heightInches: Int)
    suspend fun updateOnboardingWeight(weight: Int)
    suspend fun updateOnboardingWeightUnit(weightUnit: String)
    suspend fun updateOnboardingAge(age: String)
    suspend fun updateOnboardingRecommendedValues(
        proteins: Int,
        fats: Int,
        carbs: Int,
        calories: Int,
        waterIntake: Float
    )
    suspend fun clearOnboardingState()
    suspend fun saveCurrentDate(date: LocalDate)
    suspend fun getStoredDate(): Flow<LocalDate>

    suspend fun getCurrentMealName(): Flow<String>
    suspend fun setCurrentMealName(name: String)
    suspend fun getCurrentMealFoods(): Flow<List<FoodUiState>>
    suspend fun getCurrentMealTotals(): Flow<MealTotals>
    suspend fun addFoodToCurrentMeal(food: FoodUiState)
    suspend fun removeFoodFromCurrentMeal(index: Int)
    suspend fun clearCurrentMeal()

    suspend fun saveRecipes(recipeResponse: RecipeResponse)
    suspend fun getStoredRecipes(): Flow<RecipeResponse?>
    suspend fun getRecipeByiD(recipeId: Int): Recipe
    suspend fun deleteRecipes()
    suspend fun saveSearchQuery(query: String)
    suspend fun getLastSearchQuery(): Flow<String>

    suspend fun saveScrollPosition(firstVisibleItemIdx: Int, firstVisibleItemOffset: Int)
    suspend fun getScrollPosition(): Flow<Pair<Int, Int>>

}

data class MealTotals(
    val calories: Float = 0f,
    val proteins: Float = 0f,
    val fats: Float = 0f,
    val carbs: Float = 0f
)