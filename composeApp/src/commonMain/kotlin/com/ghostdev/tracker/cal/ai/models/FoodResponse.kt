package com.ghostdev.tracker.cal.ai.models

import kotlinx.serialization.Serializable

@Serializable
data class FoodResponse(
    val items: List<FoodItem>
)

@Serializable
data class FoodItem(
    val name: String,
    val calories: Double,
    val fat_total_g: Double,
    val protein_g: Double,
    val carbohydrates_total_g: Double,
    val serving_size_g: Double
)
