package com.ghostdev.tracker.cal.ai.models

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val from: Int,
    val to: Int,
    val count: Int,
    val hits: List<Hit>
)

@Serializable
data class Hit(
    val recipe: Recipe
)

@Serializable
data class Recipe(
    val label: String,
    val image: String,
    val shareAs: String,
    val ingredientLines: List<String>,
    val calories: Double,
    val totalNutrients: Map<String, TotalNutrient>
)

@Serializable
data class TotalNutrient(
    val label: String,
    val quantity: Double,
    val unit: String
)
