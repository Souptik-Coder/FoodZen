package com.example.foody.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.MyApplication
import com.example.foody.models.Recipe
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.NetworkResults
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
        Log.e("MainViewModel","Instance Created")
        getFavouriteRecipes()
        getAllRecentlyVisitedRecipes()
    }

    private val context by lazy { getApplication<MyApplication>().applicationContext }

    private val _recipeResponse: MutableLiveData<NetworkResults<List<Recipe>>> = MutableLiveData()
    val recipeResponse: LiveData<NetworkResults<List<Recipe>>> = _recipeResponse
    val favouriteRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val recentlyVisitedRecipes: MutableLiveData<List<Recipe>> = MutableLiveData(emptyList())


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        _recipeResponse.value = NetworkResults.Loading()
        _recipeResponse.value = recipeUseCases.getRecipes(queries)
    }


    fun getRecipeById(id: Int) = viewModelScope.launch {
        _recipeResponse.value = NetworkResults.Loading()
        _recipeResponse.value = recipeUseCases.getRecipeById(id)
    }

    private fun getFavouriteRecipes() = viewModelScope.launch {
        recipeUseCases.getFavouriteRecipes().collect {
            favouriteRecipes.value = it
        }
    }

    fun insertFavouriteRecipe(recipe: Recipe) = viewModelScope.launch {
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