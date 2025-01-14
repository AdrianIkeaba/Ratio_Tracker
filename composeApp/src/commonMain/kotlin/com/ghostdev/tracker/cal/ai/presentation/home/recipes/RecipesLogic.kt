package com.ghostdev.tracker.cal.ai.presentation.home.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.data.repo.recipe.RecipeRepo
import com.ghostdev.tracker.cal.ai.data.storage.Storage
import com.ghostdev.tracker.cal.ai.models.RecipeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class RecipesLogic : ViewModel(), KoinComponent {
    private val recipeRepo: RecipeRepo by inject()
    private val databaseRepo: DatabaseRepo by inject()
    private val storage: Storage by inject()

    private val _recipeState = MutableStateFlow(RecipeUiState())
    val recipeState: StateFlow<RecipeUiState> = _recipeState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    var firstVisibleItemIdx = 0
    var firstVisibleItemOffset = 0

    init {
        getUserName()
        viewModelScope.launch {
            storage.getLastSearchQuery().collect { query ->
                _searchQuery.value = query
            }
        }

        viewModelScope.launch {
            storage.getStoredRecipes().collect { recipes ->
                _recipeState.value = _recipeState.value.copy(
                    recipes = recipes
                )
            }
        }

        viewModelScope.launch {
            storage.getScrollPosition().collect { (index, offset) ->
                firstVisibleItemIdx = index
                firstVisibleItemOffset = offset
            }
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            val userName = databaseRepo.getUserName().toString()
            _recipeState.value = _recipeState.value.copy(userName = userName)
        }
    }

    fun getRecipes(query: String) {
        viewModelScope.launch {
            try {
                _recipeState.value = _recipeState.value.copy(loading = true)
                storage.saveSearchQuery(query)
                _searchQuery.value = query
                val response = recipeRepo.getRecipe(query)
                storage.saveRecipes(response)
                _recipeState.value = _recipeState.value.copy(
                    recipes = response,
                    loading = false
                )
            } catch (e: Exception) {
                _recipeState.value = _recipeState.value.copy(
                    loading = false
                )
            }
        }
    }

    fun saveScrollPosition(index: Int, offset: Int) {
        viewModelScope.launch {
            storage.saveScrollPosition(index, offset)
            firstVisibleItemIdx = index
            firstVisibleItemOffset = offset
        }
    }
}

data class RecipeUiState(
    val loading: Boolean = false,
    val userName: String? = null,
    val recipes: RecipeResponse? = null
)