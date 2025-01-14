package com.ghostdev.tracker.cal.ai.data.storage

import com.ghostdev.tracker.cal.ai.models.Recipe
import com.ghostdev.tracker.cal.ai.models.RecipeResponse
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.OnboardingUiState
import com.ghostdev.tracker.cal.ai.presentation.home.diary.meals.FoodUiState
import com.ghostdev.tracker.cal.ai.utilities.getCurrentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDate


class InMemoryStorage : Storage {
    private val userName = MutableStateFlow("")
    private val onboardingState = MutableStateFlow(OnboardingUiState())
    private val currentDate = MutableStateFlow(getCurrentDate())
    private val storedRecipes = MutableStateFlow<RecipeResponse?>(null)
    private val lastSearchQuery = MutableStateFlow("")
    private val scrollPosition = MutableStateFlow(Pair(0, 0))
    private val currentMealFoods = MutableStateFlow<List<FoodUiState>>(emptyList())
    private val currentMealTotals = MutableStateFlow(MealTotals())
    private val currentMealName = MutableStateFlow("Meal name here")

    override suspend fun getUserName(): Flow<String> = userName

    override suspend fun updateUserName(name: String) {
        userName.value = name
    }


    override suspend fun getOnboardingState(): Flow<OnboardingUiState> = onboardingState

    override suspend fun updateOnboardingName(name: String) {
        onboardingState.value = onboardingState.value.copy(name = name)
    }

    override suspend fun updateOnboardingGoal(goal: String) {
        onboardingState.value = onboardingState.value.copy(goal = goal)
    }

    override suspend fun updateOnboardingGender(gender: String) {
        onboardingState.value = onboardingState.value.copy(gender = gender)
    }

    override suspend fun updateOnboardingActivity(activity: String) {
        onboardingState.value = onboardingState.value.copy(activity = activity)
    }

    override suspend fun updateOnboardingHeightFeet(heightFeet: Int) {
        onboardingState.value = onboardingState.value.copy(heightFeet = heightFeet)
    }

    override suspend fun updateOnboardingHeightInches(heightInches: Int) {
        onboardingState.value = onboardingState.value.copy(heightInches = heightInches)
    }

    override suspend fun updateOnboardingWeight(weight: Int) {
        onboardingState.value = onboardingState.value.copy(weight = weight)
    }

    override suspend fun updateOnboardingWeightUnit(weightUnit: String) {
        onboardingState.value = onboardingState.value.copy(weightUnit = weightUnit)
    }

    override suspend fun updateOnboardingAge(age: String) {
        onboardingState.value = onboardingState.value.copy(age = age)
    }

    override suspend fun updateOnboardingRecommendedValues(
        proteins: Int,
        fats: Int,
        carbs: Int,
        calories: Int,
        waterIntake: Float
    ) {
        onboardingState.value = onboardingState.value.copy(
            recommendedProteins = proteins,
            recommendedFats = fats,
            recommendedCarbs = carbs,
            recommendedCalories = calories,
            recommendedWaterIntake = waterIntake
        )
    }

    override suspend fun clearOnboardingState() {
        onboardingState.value = OnboardingUiState()
    }
    override suspend fun saveCurrentDate(date: LocalDate) {
        currentDate.value = date
    }

    override suspend fun getStoredDate(): Flow<LocalDate> = currentDate

    override suspend fun saveRecipes(recipeResponse: RecipeResponse) {
        storedRecipes.value = recipeResponse
    }

    override suspend fun getStoredRecipes(): Flow<RecipeResponse?> = storedRecipes

    override suspend fun getRecipeByiD(recipeId: Int): Recipe {
        return storedRecipes.value?.hits?.get(recipeId)?.recipe
            ?: throw IllegalStateException("Recipe not found")
    }

    override suspend fun deleteRecipes() {
        storedRecipes.value = null
    }

    override suspend fun saveSearchQuery(query: String) {
        lastSearchQuery.value = query
    }

    override suspend fun getLastSearchQuery(): Flow<String> = lastSearchQuery


    override suspend fun saveScrollPosition(
        firstVisibleItemIdx: Int,
        firstVisibleItemOffset: Int
    ) {
        scrollPosition.value = Pair(firstVisibleItemIdx, firstVisibleItemOffset)
    }

    override suspend fun getScrollPosition(): Flow<Pair<Int, Int>> = scrollPosition




    override suspend fun getCurrentMealFoods(): Flow<List<FoodUiState>> = currentMealFoods

    override suspend fun getCurrentMealTotals(): Flow<MealTotals> = currentMealTotals

    override suspend fun addFoodToCurrentMeal(food: FoodUiState) {
        val currentFoods = currentMealFoods.value.toMutableList()
        currentFoods.add(food)
        currentMealFoods.value = currentFoods

        updateMealTotals()
    }

    override suspend fun removeFoodFromCurrentMeal(index: Int) {
        val currentFoods = currentMealFoods.value.toMutableList()
        if (index < currentFoods.size) {
            currentFoods.removeAt(index)
            currentMealFoods.value = currentFoods

            updateMealTotals()
        }
    }

    override suspend fun clearCurrentMeal() {
        currentMealFoods.value = emptyList()
        currentMealTotals.value = MealTotals()
        currentMealName.value = "Meal name here"
    }

    private fun updateMealTotals() {
        val foods = currentMealFoods.value
        currentMealTotals.value = MealTotals(
            calories = foods.sumOf { it.calories.toDouble() }.toFloat(),
            proteins = foods.sumOf { it.proteins.toDouble() }.toFloat(),
            fats = foods.sumOf { it.fats.toDouble() }.toFloat(),
            carbs = foods.sumOf { it.carbs.toDouble() }.toFloat()
        )
    }

    override suspend fun getCurrentMealName(): Flow<String> = currentMealName

    override suspend fun setCurrentMealName(name: String) {
        currentMealName.value = name
    }

}