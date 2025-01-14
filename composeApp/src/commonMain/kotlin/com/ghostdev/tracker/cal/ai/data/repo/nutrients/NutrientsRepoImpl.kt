package com.ghostdev.tracker.cal.ai.data.repo.nutrients

import com.ghostdev.tracker.cal.ai.data.api.MealsApi
import com.ghostdev.tracker.cal.ai.models.FoodResponse

class NutrientsRepoImpl(
    private val mealsApi: MealsApi
) : NutrientsRepo {
    override suspend fun getMealNutrients(query: String): FoodResponse{
        return mealsApi.getMealNutrients(query)
    }
}