package com.ghostdev.tracker.cal.ai.presentation.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.data.storage.Storage
import com.ghostdev.tracker.cal.ai.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileLogic: ViewModel(), KoinComponent {
    private val databaseRepo: DatabaseRepo by inject()
    private val storage: Storage by inject()

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState


    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            val user = databaseRepo.getUser()
            _profileState.value = _profileState.value.copy(
                user = user,
                isLoading = false
            )
        }
    }


    fun updateName(name: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                name = name
            ))
        }
    }

    fun updateGoal(goal: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(goal = goal))
        }
    }

    fun updateGender(gender: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                gender = gender
            ))
        }
    }

    fun updateActivity(activity: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                activityLevel = activity
            ))
        }
    }

    fun updateHeightFeet(heightFeet: Int) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                heightFeet = heightFeet
            ))
        }
    }

    fun updateHeightInches(heightInches: Int) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                heightInches = heightInches
            ))
        }
    }

    fun updateWeight(weight: Float) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                weight = weight
            ))
        }
    }

    fun updateWeightUnit(weightUnit: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(
                weightUnit = weightUnit
            ))
        }
    }

    fun updateAge(age: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(user = _profileState.value.user?.copy(age = age.toInt()))
        }
    }

    suspend fun saveUserDetails() {
        val currentState = _profileState.value
        val user = currentState.user

        // Extract values for calculation
        val weight = user!!.weight
        val heightFeet = user.heightFeet
        val heightInches = user.heightInches
        val age = user.age
        val gender = user.gender
        val activity = user.activityLevel
        val goal = user.goal
        val weightUnit = user.weightUnit

        val heightCm = (heightFeet * 30.48) + (heightInches * 2.54)
        val weightKg = if (weightUnit == "lbs") weight * 0.453592 else weight

        val bmr = when (gender) {
            "Male" -> 88.362 + (13.397 * weightKg.toDouble()) + (4.799 * heightCm) - (5.677 * age)
            "Female" -> 447.593 + (9.247 * weightKg.toDouble()) + (3.098 * heightCm) - (4.330 * age)
            else -> 0.0
        }


        val totalCalories = when (activity) {
            "Sedentary" -> bmr * 1.2
            "Low active" -> bmr * 1.375
            "Active" -> bmr * 1.55
            "Very active" -> bmr * 1.725
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
        val waterGrams = if (gender == "Male") 3.7f else 2.7f

        val updatedUser = user.copy(
            recommendedCalories = totalCalories.toInt(),
            recommendedProteins = proteinGrams,
            recommendedFats = fatGrams,
            recommendedCarbs = carbGrams,
            recommendedWaterIntake = waterGrams
        )

        _profileState.value = currentState.copy(user = updatedUser)
        databaseRepo.saveUser(updatedUser)
        storage.clearOnboardingState()
    }
}

data class ProfileUiState(
    val user: User? = null,
    val calories: Int? = null,
    val waterServingSize: Int? = null,
    val isLoading: Boolean = true,
)