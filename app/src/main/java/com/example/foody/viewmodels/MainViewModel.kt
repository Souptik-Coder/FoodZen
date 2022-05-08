package com.example.foody.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.models.Recipe
import com.example.foody.use_cases.RecipeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val recipeUseCases: RecipeUseCases,
) : AndroidViewModel(application) {
    init {
        Log.e("MainViewModel", "Instance Created")
        getFavouriteRecipes()
        getAllRecentlyVisitedRecipes()
    }

    val favouriteRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val recentlyVisitedRecipes: MutableLiveData<List<Recipe>> = MutableLiveData(emptyList())


    private fun getFavouriteRecipes() = viewModelScope.launch {
        recipeUseCases.getFavouriteRecipes().collect {
            favouriteRecipes.value = it
        }
    }

    fun insertOrUpdateFavouriteRecipe(recipe: Recipe) = viewModelScope.launch {
        recipeUseCases.insertFavouriteRecipes(recipe)
    }

    fun deleteFavouriteRecipe(recipe: Recipe) = viewModelScope.launch {
        recipeUseCases.deleteFavouriteRecipes(recipe)
    }

    private fun getAllRecentlyVisitedRecipes() = viewModelScope.launch {
        recipeUseCases.getAllRecentlyVisitedRecipes().collect {
            recentlyVisitedRecipes.value = it
        }
    }
}