package com.example.foody.ui.screens.recipes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.data.repositories.DataStoreRepository
import com.example.foody.models.Recipe
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.Constants.API_KEY
import com.example.foody.util.Constants.DEFAULT_RECIPES_NUMBER
import com.example.foody.util.Constants.DEFAULT_SORT
import com.example.foody.util.Constants.DEFAULT_SORT_DIRECTIONS
import com.example.foody.util.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody.util.Constants.QUERY_API_KEY
import com.example.foody.util.Constants.QUERY_CUISINE
import com.example.foody.util.Constants.QUERY_DIET
import com.example.foody.util.Constants.QUERY_FILL_INGREDIENTS
import com.example.foody.util.Constants.QUERY_INTOLERANCES
import com.example.foody.util.Constants.QUERY_NUMBER
import com.example.foody.util.Constants.QUERY_SORT
import com.example.foody.util.Constants.QUERY_SORT_DIRECTIONS
import com.example.foody.util.Constants.QUERY_TYPE
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    init {
        Log.e("RecipesViewModel", "Instance Created")
    }

    val recipeByIdResponse = MutableLiveData<NetworkResults<Recipe>>()
    val topRecipeResponse = MutableLiveData<NetworkResults<List<Recipe>>>()
    val readRecipeFilterParameter =
        dataStoreRepository.readRecipeFilterParameter.distinctUntilChanged()

    init {
        observeRecipeParameter()
    }

    fun saveMealAndDietType(
        recipeFilterParameters: DataStoreRepository.RecipeFilterParameters
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveRecipeFilterParameters(recipeFilterParameters)
    }

    private fun applyQueries(recipeFilterParameters: DataStoreRepository.RecipeFilterParameters):
            HashMap<String, String> {
        val queries = HashMap<String, String>()
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_TYPE] = recipeFilterParameters.selectedMealType
        queries[QUERY_DIET] = recipeFilterParameters.selectedDietType
        queries[QUERY_CUISINE] = recipeFilterParameters.selectedCuisineType
        queries[QUERY_INTOLERANCES] = recipeFilterParameters.selectedIntoleranceType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_SORT] = DEFAULT_SORT
        queries[QUERY_SORT_DIRECTIONS] = DEFAULT_SORT_DIRECTIONS
        return queries
    }

    fun getRecipeById(id: Int) = viewModelScope.launch {
        recipeByIdResponse.value = NetworkResults.Loading()
        recipeByIdResponse.value = recipeUseCases.getRecipeById(id)
    }

    private fun getTopRecipes(queries: Map<String, String>) = viewModelScope.launch {
        topRecipeResponse.value = NetworkResults.Loading()
        topRecipeResponse.value = recipeUseCases.getRecipes(queries)
    }

    fun observeRecipeParameter() = viewModelScope.launch {
        readRecipeFilterParameter.collectLatest {
            getTopRecipes(applyQueries(it))
        }
    }
}