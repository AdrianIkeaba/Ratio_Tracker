package com.ghostdev.tracker.cal.ai.data.repo.recipe

import com.ghostdev.tracker.cal.ai.models.RecipeResponse

interface RecipeRepo {
    suspend fun getRecipe(query: String): RecipeResponse
}