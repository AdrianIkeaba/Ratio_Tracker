package com.ghostdev.tracker.cal.ai.data.repo.nutrients

import com.ghostdev.tracker.cal.ai.models.FoodResponse

interface NutrientsRepo {
    suspend fun getMealNutrients(query: String): FoodResponse
}