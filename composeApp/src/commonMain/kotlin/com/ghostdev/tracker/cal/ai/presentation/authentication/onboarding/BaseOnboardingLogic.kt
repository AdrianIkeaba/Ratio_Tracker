package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.data.repo.user.UserRepo
import com.ghostdev.tracker.cal.ai.data.storage.Storage
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.models.Weights
import com.ghostdev.tracker.cal.ai.utilities.getCurrentDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BaseOnboardingLogic : ViewModel(), KoinComponent {
    private val databaseRepo: DatabaseRepo by inject()
    private val userRepo: UserRepo by inject()
    private val storage: Storage by inject()

    private val _onboardingState = MutableStateFlow(OnboardingUiState())
    val onboardingState: StateFlow<OnboardingUiState> = _onboardingState

    init {
        viewModelScope.launch {
            storage.getOnboardingState().collect { state ->
                _onboardingState.value = state
            }
        }
    }

    fun setUserExists() {
        viewModelScope.launch {
            userRepo.setUserExists(true)
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            storage.updateOnboardingName(name)
        }
    }

    fun updateGoal(goal: String) {
        viewModelScope.launch {
            storage.updateOnboardingGoal(goal)
        }
    }

    fun updateGender(gender: String) {
        viewModelScope.launch {
            storage.updateOnboardingGender(gender)
        }
    }

    fun updateActivity(activity: String) {
        viewModelScope.launch {
            storage.updateOnboardingActivity(activity)
        }
    }

    fun updateHeightFeet(heightFeet: Int) {
        viewModelScope.launch {
            storage.updateOnboardingHeightFeet(heightFeet)
        }
    }

    fun updateHeightInches(heightInches: Int) {
        viewModelScope.launch {
            storage.updateOnboardingHeightInches(heightInches)
        }
    }

    fun updateWeight(weight: Int) {
        viewModelScope.launch {
            storage.updateOnboardingWeight(weight)
        }
    }

    fun updateWeightUnit(weightUnit: String) {
        viewModelScope.launch {
            storage.updateOnboardingWeightUnit(weightUnit)
        }
    }

    fun updateAge(age: String) {
        viewModelScope.launch {
            storage.updateOnboardingAge(age)
        }
    }

    suspend fun saveUserDetails() {
        val currentState = onboardingState.value

        val user = User(
            name = currentState.name ?: "",
            age = currentState.age?.toIntOrNull() ?: 0,
            gender = currentState.gender ?: "Unknown",
            heightFeet = currentState.heightFeet ?: 0,
            heightInches = currentState.heightInches ?: 0,
            weight = currentState.weight?.toFloat() ?: 0f,
            weightUnit = currentState.weightUnit ?: "kg",
            goal = currentState.goal ?: "None",
            activityLevel = currentState.activity ?: "Unknown",
            recommendedProteins = currentState.recommendedProteins ?: 0,
            recommendedFats = currentState.recommendedFats ?: 0,
            recommendedCarbs = currentState.recommendedCarbs ?: 0,
            recommendedCalories = currentState.recommendedCalories ?: 0,
            recommendedWaterIntake = currentState.recommendedWaterIntake ?: 0f,
        )

        databaseRepo.saveUser(user)
        databaseRepo.addWeight(
            Weights(
                id = 0,
                weight = user.weight,
                date = getCurrentDate()
            )
        )
        storage.clearOnboardingState()
    }

    fun calculatePFC(userDetails: OnboardingUiState): Map<String, Int>? {
        val weight = userDetails.weight ?: return null
        val heightFeet = userDetails.heightFeet ?: return null
        val heightInches = userDetails.heightInches ?: return null
        val age = userDetails.age?.toIntOrNull() ?: return null
        val gender = userDetails.gender ?: return null
        val activity = userDetails.activity ?: return null
        val goal = userDetails.goal ?: return null

        val heightCm = (heightFeet * 30.48) + (heightInches * 2.54)
        val weightKg = if (userDetails.weightUnit == "lbs") weight * 0.453592 else weight.toDouble()

        val bmr = when (gender) {
            "Male" -> 88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * age)
            "Female" -> 447.593 + (9.247 * weightKg) + (3.098 * heightCm) - (4.330 * age)
            else -> return null
        }

        val totalCalories = when (activity) {
            "Sedentary" -> bmr * 1.2
            "Low active" -> bmr * 1.375
            "Active" -> bmr * 1.55
            "Very active" -> bmr * 1.825
            else -> bmr
        }

        val (proteinRatio, fatRatio, carbRatio) = when (goal) {
            "Lose weight" -> Triple(0.4, 0.3, 0.3)
            "Keep weight" -> Triple(0.3, 0.3, 0.4)
            "Gain weight" -> Triple(0.45, 0.2, 0.35)
            else -> Triple(0.3, 0.3, 0.4)
        }

        val proteinGrams = (totalCalories * proteinRatio / 4).toInt()
        val fatGrams = (totalCalories * fatRatio / 9).toInt()
        val carbGrams = (totalCalories * carbRatio / 4).toInt()
        val waterGrams = if (gender == "Male") 3.7f else 2.5f

        viewModelScope.launch {
            storage.updateOnboardingRecommendedValues(
                proteins = proteinGrams,
                fats = fatGrams,
                carbs = carbGrams,
                calories = totalCalories.toInt(),
                waterIntake = waterGrams
            )
        }

        return mapOf(
            "protein" to proteinGrams,
            "fat" to fatGrams,
            "carbs" to carbGrams,
            "calories" to totalCalories.toInt()
        )
    }
}


data class OnboardingUiState(
    val name: String? = null,
    val goal: String? = null,
    val gender: String? = null,
    val activity: String? = null,
    val heightFeet: Int? = null,
    val heightInches: Int? = null,
    val weight: Int? = null,
    val weightUnit: String? = null,
    val age: String? = null,
    val recommendedProteins: Int? = null,
    val recommendedFats: Int? = null,
    val recommendedCarbs: Int? = null,
    val recommendedCalories: Int? = null,
    val recommendedWaterIntake: Float? = null
)