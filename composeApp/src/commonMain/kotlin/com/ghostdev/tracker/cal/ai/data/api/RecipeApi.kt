package com.ghostdev.tracker.cal.ai.data.api

import com.ghostdev.tracker.cal.ai.models.RecipeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

interface RecipeApi {
    suspend fun getRecipe(query: String): RecipeResponse
}

class KtorRecipeApi(private val client: HttpClient) : RecipeApi {
    companion object {
        private const val API_URL = "https://api.edamam.com/api/recipes/v2"
        private const val APP_ID = "your_app_id_here"
        private const val APP_KEY = "your_app_key_here"
    }

    override suspend fun getRecipe(query: String): RecipeResponse {
        val url = "$API_URL?type=public&q=$query&app_id=$APP_ID&app_key=$APP_KEY"
        return try {
            val response = client.get {
                url(url)
            }.body<RecipeResponse>()
            response
        } catch (e: Exception) {
            println("Error fetching recipes: ${e.message}")
            RecipeResponse(0, 0, 0, emptyList())
        }
    }
}