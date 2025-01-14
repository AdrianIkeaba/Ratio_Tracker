package com.ghostdev.tracker.cal.ai.presentation.home.profile.calories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CalorieLogic: ViewModel(), KoinComponent {
    private val databaseRepo: DatabaseRepo by inject()

    private val _calorieState = MutableStateFlow(CalorieUiState())
    val calorieState: StateFlow<CalorieUiState> = _calorieState


    fun getCalorieData() {
        viewModelScope.launch {
            _calorieState.value = CalorieUiState(isLoading = true)
            val calorieData = databaseRepo.getUser()
            _calorieState.value = CalorieUiState(
                isLoading = false,
                calorie = calorieData?.recommendedCalories.toString(),
                protein = calorieData?.recommendedProteins.toString(),
                carbs = calorieData?.recommendedCarbs.toString(),
                fat = calorieData?.recommendedFats.toString(),
                water = calorieData?.recommendedWaterIntake.toString()
            )
        }
    }

    fun updateCalorieData(calorie: String) {
        viewModelScope.launch {
            _calorieState.value = _calorieState.value.copy(calorie = calorie)
        }
    }

    fun updateProteinData(protein: String) {
        viewModelScope.launch {
            _calorieState.value = _calorieState.value.copy(protein = protein)
        }
    }

    fun updateCarbsData(carbs: String) {
        viewModelScope.launch {
            _calorieState.value = _calorieState.value.copy(carbs = carbs)
        }
    }

    fun updateFatData(fat: String) {
        viewModelScope.launch {
            _calorieState.value = _calorieState.value.copy(fat = fat)
        }
    }

    fun updateWaterData(water: String) {
        viewModelScope.launch {
            _calorieState.value = _calorieState.value.copy(water = water)
        }
    }

    fun saveUserCaloriesData() {
        viewModelScope.launch {
            val currentCaloriesDataState = _calorieState.value
            val existingUserData = databaseRepo.getUser()

            val updatedUserData = existingUserData?.copy(
                recommendedCalories = currentCaloriesDataState.calorie?.toInt() ?: 0,
                recommendedProteins = currentCaloriesDataState.protein?.toInt() ?: 0,
                recommendedCarbs = currentCaloriesDataState.carbs?.toInt() ?: 0,
                recommendedFats = currentCaloriesDataState.fat?.toInt() ?: 0,
                recommendedWaterIntake = currentCaloriesDataState.water?.toFloat() ?: 0f
            )

            databaseRepo.saveUser(updatedUserData!!)
        }
    }
}

data class CalorieUiState(
    val isLoading: Boolean = false,
    val calorie: String? = null,
    val protein: String? = null,
    val carbs: String? = null,
    val fat: String? = null,
    val water: String? = null
)