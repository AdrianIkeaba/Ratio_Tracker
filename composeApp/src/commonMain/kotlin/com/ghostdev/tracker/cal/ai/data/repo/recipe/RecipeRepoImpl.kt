package com.ghostdev.tracker.cal.ai.data.repo.recipe

import com.ghostdev.tracker.cal.ai.data.api.RecipeApi
import com.ghostdev.tracker.cal.ai.models.RecipeResponse

class RecipeRepoImpl(
    private val recipeApi: RecipeApi
) : RecipeRepo {
    override suspend fun getRecipe(query: String): RecipeResponse {
        return recipeApi.getRecipe(query)
    }
}