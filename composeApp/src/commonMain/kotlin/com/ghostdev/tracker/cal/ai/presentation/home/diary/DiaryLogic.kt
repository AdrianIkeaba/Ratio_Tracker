package com.ghostdev.tracker.cal.ai.presentation.home.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.data.storage.Storage
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.models.WaterIntake
import com.ghostdev.tracker.cal.ai.utilities.getCurrentDate
import com.ghostdev.tracker.cal.ai.utilities.getCurrentTime
import com.ghostdev.tracker.cal.ai.utilities.getCurrentTimeFormatted
import com.ghostdev.tracker.cal.ai.utilities.roundToDecimals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DiaryLogic: ViewModel(), KoinComponent {
    private val databaseRepo: DatabaseRepo by inject()
    private val storage: Storage by inject()

    private val _homeState = MutableStateFlow(HomeUiState(selectedDate = getCurrentDate()))
    val homeState: StateFlow<HomeUiState> = _homeState
    private val _currentDate = MutableStateFlow("Today")
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    init {
        viewModelScope.launch {
            val storedDate = storage.getStoredDate().firstOrNull() ?: getCurrentDate()
            _homeState.value = _homeState.value.copy(selectedDate = storedDate)
            updateDataForSelectedDate()
        }
    }

    fun fetchDataForDate(date: LocalDate) {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(selectedDate = date)
            storage.saveCurrentDate(date)
            getUserName()
            getNutrientsData(date)
            getWaterIntakeData(date)
            getMealsData(date)
            _homeState.value = _homeState.value.copy(isLoading = false)
        }
    }

    fun updateCurrentDate(newDate: String) {
        _currentDate.value = newDate
    }

    private fun updateDataForSelectedDate() {
        val selectedDate = _homeState.value.selectedDate
        getUserName()
        getNutrientsData(selectedDate)
        getWaterIntakeData(selectedDate)
        getMealsData(selectedDate)
        _homeState.value = _homeState.value.copy(isLoading = false)
    }

    private fun getUserName() {
        viewModelScope.launch {
            val userName = databaseRepo.getUserName().toString()
            _homeState.value = _homeState.value.copy(userName = userName)
        }
    }

    private fun getNutrientsData(date: LocalDate) {
        viewModelScope.launch {
            val recommendedUserData = databaseRepo.getUser()
            val nutrientsData = databaseRepo.getMealsByDate(date = date)
            val totalCalories = nutrientsData.sumOf { it.totalCalories.toInt() }
            val totalProteins = nutrientsData.sumOf { it.totalProteins.toInt() }
            val totalFats = nutrientsData.sumOf { it.totalFats.toInt() }
            val totalCarbs = nutrientsData.sumOf { it.totalCarbs.toInt() }

            _homeState.value = _homeState.value.copy(nutrientsIndicator = NutrientsIndicatorState(
                currentCalories = totalCalories,
                currentProteins = totalProteins,
                currentFats = totalFats,
                currentCarbs = totalCarbs,

                recommendedCalories = recommendedUserData!!.recommendedCalories,
                recommendedProteins = recommendedUserData.recommendedProteins,
                recommendedCarbs = recommendedUserData.recommendedCarbs,
                recommendedFats = recommendedUserData.recommendedFats
            ))
        }
    }

    private fun getWaterIntakeData(date: LocalDate) {
        viewModelScope.launch {
            val waterIntakeData = databaseRepo.getWaterIntake(date = date)
            val recommendedUserData = databaseRepo.getUser()

            val currentIntake = (waterIntakeData?.currentIntake ?: 0f).roundToDecimals(1)
            val recommendedIntake = recommendedUserData?.recommendedWaterIntake ?: 1f

            _homeState.value = _homeState.value.copy(
                selectedDateWaterIntake = waterIntakeData,
                waterIntake = WaterIntakeState(
                    currentWaterIntake = currentIntake,
                    recommendedWaterIntake = recommendedIntake,
                    lastUpdatedTime = waterIntakeData?.lastUpdatedTime,
                    currentWaterIntakePercentage = ((currentIntake / recommendedIntake) * 100).toInt()
                )
            )
        }
    }

    private fun getMealsData(date: LocalDate) {
        viewModelScope.launch {
            val mealData = databaseRepo.getMealsByDate(date = date)
            _homeState.value = _homeState.value.copy(
                meals = MealsState(
                    meals = mealData.map { meal ->
                        MealState(
                            name = meal.name,
                            totalCalories = meal.totalCalories.toInt(),
                            date = meal.date.toString(),
                            time = meal.time.toString(),
                            meal = meal
                        )
                    }
                )
            )
        }
    }

    fun updateWaterIntake(value: Float, date: LocalDate) {
        viewModelScope.launch {
            val existingWaterIntake = _homeState.value.selectedDateWaterIntake ?:
            databaseRepo.getWaterIntake(date = date)

            val currentValue = existingWaterIntake?.currentIntake ?: 0f
            val newValue = (currentValue + value).coerceAtLeast(0f)
            val currentIntake = newValue.roundToDecimals(1)
            val recommendedIntake = homeState.value.waterIntake?.recommendedWaterIntake ?: 1f

            val updatedWaterIntake = WaterIntake(
                id = existingWaterIntake?.id ?: 0,
                date = date,
                currentIntake = currentIntake,
                lastUpdatedTime = getCurrentTimeFormatted()
            )

            databaseRepo.updateWaterIntake(updatedWaterIntake)

            _homeState.value = _homeState.value.copy(
                selectedDateWaterIntake = updatedWaterIntake,
                waterIntake = WaterIntakeState(
                    currentWaterIntake = currentIntake,
                    recommendedWaterIntake = recommendedIntake,
                    lastUpdatedTime = getCurrentTimeFormatted(),
                    currentWaterIntakePercentage = ((currentIntake / recommendedIntake) * 100).toInt()
                )
            )
        }
    }
}


data class HomeUiState(
    val userName: String? = null,
    val selectedDate: LocalDate = getCurrentDate(),
    val currentTime: LocalTime = getCurrentTime(),
    val selectedDateWaterIntake: WaterIntake? = null,
    val nutrientsIndicator: NutrientsIndicatorState? = null,
    val waterIntake: WaterIntakeState? = null,
    val meals: MealsState? = null,
    val isLoading: Boolean = true
)

data class NutrientsIndicatorState(
    val currentProteins: Int? = null,
    val currentFats: Int? = null,
    val currentCarbs: Int? = null,
    val currentCalories: Int? = null,


    val recommendedProteins: Int? = null,
    val recommendedFats: Int? = null,
    val recommendedCarbs: Int? = null,
    val recommendedCalories: Int? = null,
)

data class WaterIntakeState(
    val currentWaterIntake: Float? = null,
    val recommendedWaterIntake: Float? = null,
    val lastUpdatedTime: String? = null,
    val currentWaterIntakePercentage: Int? = null
)

data class MealsState(
    val meals: List<MealState>? = null
)

data class MealState(
    val name: String? = null,
    val totalCalories: Int? = null,
    val date: String? = null,
    val time: String? = null,
    val meal: Meal
)