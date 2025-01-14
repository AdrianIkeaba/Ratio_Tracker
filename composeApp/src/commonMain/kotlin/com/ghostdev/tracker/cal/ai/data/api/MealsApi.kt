package com.ghostdev.tracker.cal.ai.data.api

import com.ghostdev.tracker.cal.ai.models.FoodResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

interface MealsApi {
    suspend fun getMealNutrients(query: String): FoodResponse
}

class KtorMealsApi(private val client: HttpClient) : MealsApi {
    companion object {
        private const val API_URL = "https://api.calorieninjas.com/v1/nutrition"
    }

    override suspend fun getMealNutrients(query: String): FoodResponse {
        return try {
            client.get("$API_URL?query=$query") {
                header("X-Api-Key", "VHUZAxJTTJHD0SRn/STJ9A==IilUb3uLmfdJAbCb")
            }.body()
        } catch (e: Exception) {
            println("Error Message: ${e.message}")
            FoodResponse(emptyList())
        }
    }
}