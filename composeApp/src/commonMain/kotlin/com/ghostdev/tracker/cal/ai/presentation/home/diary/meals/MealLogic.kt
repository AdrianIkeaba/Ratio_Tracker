package com.ghostdev.tracker.cal.ai.presentation.home.diary.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.data.repo.nutrients.NutrientsRepo
import com.ghostdev.tracker.cal.ai.data.storage.Storage
import com.ghostdev.tracker.cal.ai.models.Food
import com.ghostdev.tracker.cal.ai.models.FoodItem
import com.ghostdev.tracker.cal.ai.models.FoodResponse
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.utilities.getCurrentTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MealLogic : ViewModel(), KoinComponent {
    private val nutrientsRepo: NutrientsRepo by inject()
    private val databaseRepo: DatabaseRepo by inject()
    private val storage: Storage by inject()

    private val _searchMealState = MutableStateFlow(SearchFoodUiState())
    val searchMealState: StateFlow<SearchFoodUiState> = _searchMealState

    private val _createMealState = MutableStateFlow(CreateMealState())
    val createMealState: StateFlow<CreateMealState> = _createMealState

    init {
        viewModelScope.launch {
            combine(
                storage.getCurrentMealFoods(),
                storage.getCurrentMealTotals(),
                storage.getCurrentMealName()
            ) { foods, totals, mealName ->
                CreateMealState(
                    mealName = mealName,
                    foods = foods,
                    totalCalories = totals.calories,
                    totalProteins = totals.proteins,
                    totalFats = totals.fats,
                    totalCarbs = totals.carbs
                )
            }.collect { state ->
                _createMealState.value = state
            }
        }
    }


    fun loadMealForEditing(meal: Meal) {
        viewModelScope.launch {
            val currentMealName = storage.getCurrentMealName().firstOrNull()
            if (currentMealName == meal.name) {
                return@launch
            }

            storage.clearCurrentMeal()
            storage.setCurrentMealName(meal.name)

            val foods = databaseRepo.getFoodsByMeal(meal.id)

            foods.forEach { food ->
                val foodUiState = FoodUiState(
                    name = food.name,
                    servingSize = food.quantityInGrams,
                    calories = food.calories,
                    proteins = food.proteins,
                    fats = food.fats,
                    carbs = food.carbs
                )
                storage.addFoodToCurrentMeal(foodUiState)
            }
        }
    }

    suspend fun updateMeal(mealName: String, date: LocalDate, mealId: Int) {
        val currentState = createMealState.value
        storage.clearCurrentMeal()

        val updatedMeal = Meal(
            id = mealId,
            date = date,
            time = getCurrentTime(),
            name = mealName,
            totalCalories = currentState.totalCalories,
            totalProteins = currentState.totalProteins,
            totalFats = currentState.totalFats,
            totalCarbs = currentState.totalCarbs
        )

        databaseRepo.saveMeal(updatedMeal)

        val existingFoods = databaseRepo.getFoodsByMeal(mealId)
        existingFoods.forEach { food ->
            databaseRepo.deleteFood(food)
        }

        currentState.foods.forEach { foodUiState ->
            val food = Food(
                mealId = mealId,
                name = foodUiState.name,
                quantityInGrams = foodUiState.servingSize,
                calories = foodUiState.calories,
                proteins = foodUiState.proteins,
                fats = foodUiState.fats,
                carbs = foodUiState.carbs
            )
            databaseRepo.saveFood(food)
        }
    }

    fun searchFood(query: String) {
        viewModelScope.launch {
            try {
                _searchMealState.value = _searchMealState.value.copy(loading = true)
                val response = nutrientsRepo.getMealNutrients(query)
                _searchMealState.value = _searchMealState.value.copy(
                    food = response
                )
                _searchMealState.value = _searchMealState.value.copy(loading = false)
            } catch (e: Exception) {
                _searchMealState.value = _searchMealState.value.copy(loading = false)
            }
        }
    }

    fun addFoodToMeal(foodItem: FoodItem, servingSize: String) {
        viewModelScope.launch {
            val foodUiState = FoodUiState(
                name = foodItem.name,
                servingSize = servingSize.toFloatOrNull() ?: foodItem.serving_size_g.toFloat(),
                calories = foodItem.calories.toFloat(),
                proteins = foodItem.protein_g.toFloat(),
                fats = foodItem.fat_total_g.toFloat(),
                carbs = foodItem.carbohydrates_total_g.toFloat()
            )
            storage.addFoodToCurrentMeal(foodUiState)
        }
    }

    fun updateMealName(name: String) {
        viewModelScope.launch {
            storage.setCurrentMealName(name)
        }
    }

    suspend fun saveMeal(mealName: String, date: LocalDate) {
        val currentState = createMealState.value
        storage.clearCurrentMeal()

        val meal = Meal(
            date = date,
            time = getCurrentTime(),
            name = mealName,
            totalCalories = currentState.totalCalories,
            totalProteins = currentState.totalProteins,
            totalFats = currentState.totalFats,
            totalCarbs = currentState.totalCarbs,
        )

        val mealID = databaseRepo.saveMeal(meal)

        currentState.foods.forEach { foodUiState ->
            val food = Food(
                mealId = mealID.toInt(),
                name = foodUiState.name,
                quantityInGrams = foodUiState.servingSize,
                calories = foodUiState.calories,
                proteins = foodUiState.proteins,
                fats = foodUiState.fats,
                carbs = foodUiState.carbs
            )
            databaseRepo.saveFood(food)
        }
    }

    fun removeFoodFromMeal(index: Int) {
        viewModelScope.launch {
            storage.removeFoodFromCurrentMeal(index)
        }
    }

    fun clearMealState() {
        viewModelScope.launch {
            storage.clearCurrentMeal()
            _createMealState.value = CreateMealState()
        }
    }

}
data class SearchFoodUiState(
    val food: FoodResponse? = null,
    val loading: Boolean = false
)


data class CreateMealState(
    val mealName: String = "",
    val foods: List<FoodUiState> = emptyList(),
    val totalCalories: Float = 0f,
    val totalProteins: Float = 0f,
    val totalFats: Float = 0f,
    val totalCarbs: Float = 0f,
)

data class FoodUiState(
    val name: String,
    val servingSize: Float,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)