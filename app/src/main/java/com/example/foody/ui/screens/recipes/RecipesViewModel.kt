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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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

    /*Filter parameter stored in preference datastore*/
    val topRecipeFilterParameters =
        MutableLiveData<DataStoreRepository.RecipeFilterParameters>(DataStoreRepository.RecipeFilterParameters())

    /*Top recipes from database*/
    val savedTopRecipes = MutableLiveData<List<Recipe>>()

    /*Filter Parameter changed by user but not yet stored in preference datastore*/
    private var newTopRecipeFilterParameter: DataStoreRepository.RecipeFilterParameters? = null

    init {
        observeRecipeParameter()
        getSavedTopRecipes()
    }

    fun setNewTopRecipeFilterParameter(newTopRecipeFilterParameter: DataStoreRepository.RecipeFilterParameters) {
        this.newTopRecipeFilterParameter = newTopRecipeFilterParameter
        if (topRecipeFilterParameters.value != newTopRecipeFilterParameter) /*don't make api req if new filter parameter is same as old*/
            getTopRecipes(applyQueries(newTopRecipeFilterParameter))
    }

    /*
        Save filter parameter to preference datastore
    */
    private fun saveRecipeFilterParameters(
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

    /*
        Only for deep link req
    */
    fun getRecipeById(id: Int) = viewModelScope.launch {
        recipeByIdResponse.value = NetworkResults.Loading()
        recipeByIdResponse.value = recipeUseCases.getRecipeById(id)
    }

    private fun getTopRecipes(queries: Map<String, String>) = viewModelScope.launch {
        topRecipeResponse.value = NetworkResults.Loading()
        topRecipeResponse.value = recipeUseCases.getRecipes(queries)
        /*
            Save new filter parameter and top recipes only if api req is success
        */
        if (topRecipeResponse.value is NetworkResults.Success) {
            val recipes = topRecipeResponse.value!!.data
            saveAllTopRecipes(recipes!!)
            newTopRecipeFilterParameter?.let { saveRecipeFilterParameters(it) }
        }
    }

    private fun saveAllTopRecipes(topRecipes: List<Recipe>) = viewModelScope.launch {
        recipeUseCases.insertAllTopRecipes(topRecipes)
    }

    private fun getSavedTopRecipes() = viewModelScope.launch {
        recipeUseCases.getAllTopRecipes().collectLatest {
            savedTopRecipes.value = it
        }
    }

    private fun observeRecipeParameter() = viewModelScope.launch {
        dataStoreRepository.readTopRecipeFilterParameters().collect {
            topRecipeFilterParameters.value = it
        }
    }
}