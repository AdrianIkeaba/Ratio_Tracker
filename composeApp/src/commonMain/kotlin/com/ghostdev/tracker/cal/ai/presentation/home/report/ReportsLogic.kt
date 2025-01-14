package com.ghostdev.tracker.cal.ai.presentation.home.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.models.Weights
import com.ghostdev.tracker.cal.ai.utilities.getCurrentDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReportsLogic: ViewModel(), KoinComponent {
    private val databaseRepo: DatabaseRepo by inject()

    private val _reportsState = MutableStateFlow(ReportsUiState())
    val reportsState: StateFlow<ReportsUiState> = _reportsState

    init {
        viewModelScope.launch {
            getUserCurrentData()
            getReports()
            getWeights()
        }
    }

    private fun getReports() {
        viewModelScope.launch {
            _reportsState.value = _reportsState.value.copy(
                weightsReport = databaseRepo.getWeights()
            )
        }
    }


    private suspend fun getUserCurrentData() {
        val userCurrentWeightUnit = databaseRepo.getUser()!!.weightUnit
        _reportsState.value = _reportsState.value.copy(
            weightUnit = userCurrentWeightUnit
        )

        val userCurrentWeightValue = databaseRepo.getUser()!!.weight
        _reportsState.value = _reportsState.value.copy(
            currentWeight = userCurrentWeightValue
        )

        val userName = databaseRepo.getUser()!!.name
        _reportsState.value = _reportsState.value.copy(
            name = userName
        )
    }

    fun deleteWeight(weight: Weights) {
        viewModelScope.launch {
            databaseRepo.deleteWeight(weight)
            getWeights()
        }
    }

    fun addWeight(weight: Float) {
        viewModelScope.launch {
            val newWeight = Weights(
                date = getCurrentDate(),
                weight = weight
            )
            databaseRepo.addWeight(newWeight)
            getWeights()
        }
    }

    private fun getWeights() {
        viewModelScope.launch {
            _reportsState.value = _reportsState.value.copy(
                weightsReport = databaseRepo.getWeights().sortedBy { it.date }
            )
        }
    }
}

data class ReportsUiState(
    val currentWeight: Float? = null,
    val weightUnit: String? = null,
    val weightsReport: List<Weights> = emptyList(),
    val name: String? = null
)
