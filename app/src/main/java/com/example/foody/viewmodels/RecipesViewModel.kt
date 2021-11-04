package com.example.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.data.DataStoreRepository
import com.example.foody.util.Constants.API_KEY
import com.example.foody.util.Constants.DEFAULT_CUISINE
import com.example.foody.util.Constants.DEFAULT_DIET
import com.example.foody.util.Constants.DEFAULT_INTOLERANCE
import com.example.foody.util.Constants.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constants.DEFAULT_RECIPES_NUMBER
import com.example.foody.util.Constants.NO_FILTER
import com.example.foody.util.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody.util.Constants.QUERY_API_KEY
import com.example.foody.util.Constants.QUERY_CUISINE
import com.example.foody.util.Constants.QUERY_DIET
import com.example.foody.util.Constants.QUERY_FILL_INGREDIENTS
import com.example.foody.util.Constants.QUERY_INTOLERANCES
import com.example.foody.util.Constants.QUERY_NUMBER
import com.example.foody.util.Constants.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var recipeFilterParameters = MutableLiveData(
        DataStoreRepository.RecipeFilterParameters(
            DEFAULT_MEAL_TYPE, 0,
            DEFAULT_DIET, 0,
            DEFAULT_CUISINE, 0,
            DEFAULT_INTOLERANCE, 0
        )
    )
    val readRecipeFilterParameter = dataStoreRepository.readRecipeFilterParameter

    fun saveMealAndDietType(
        recipeFilterParameters: DataStoreRepository.RecipeFilterParameters
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveRecipeFilterParameters(recipeFilterParameters)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries = HashMap<String, String>()

        viewModelScope.launch {
            readRecipeFilterParameter.collect { value ->
                recipeFilterParameters.value = value
            }
        }

        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_TYPE] =
            recipeFilterParameters.value!!.selectedMealType.takeIf { it != NO_FILTER } ?: ""
        queries[QUERY_DIET] =
            recipeFilterParameters.value!!.selectedDietType.takeIf { it != NO_FILTER } ?: ""
        queries[QUERY_CUISINE] =
            recipeFilterParameters.value!!.selectedCuisineType.takeIf { it != NO_FILTER } ?: ""
        queries[QUERY_INTOLERANCES] =
            recipeFilterParameters.value!!.selectedIntoleranceType.takeIf { it != NO_FILTER } ?: ""
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}