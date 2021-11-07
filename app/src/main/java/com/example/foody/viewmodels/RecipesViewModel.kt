package com.example.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.data.repositories.DataStoreRepository
import com.example.foody.util.Constants
import com.example.foody.util.Constants.API_KEY
import com.example.foody.util.Constants.DEFAULT_RECIPES_NUMBER
import com.example.foody.util.Constants.DEFAULT_SORT
import com.example.foody.util.Constants.DEFAULT_SORT_DIRECTIONS
import com.example.foody.util.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody.util.Constants.QUERY_API_KEY
import com.example.foody.util.Constants.QUERY_CUISINE
import com.example.foody.util.Constants.QUERY_FILL_INGREDIENTS
import com.example.foody.util.Constants.QUERY_INTOLERANCES
import com.example.foody.util.Constants.QUERY_NUMBER
import com.example.foody.util.Constants.QUERY_SORT
import com.example.foody.util.Constants.QUERY_SORT_DIRECTIONS
import com.example.foody.util.Constants.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    val readRecipeFilterParameter = dataStoreRepository.readRecipeFilterParameter

    fun saveMealAndDietType(
        recipeFilterParameters: DataStoreRepository.RecipeFilterParameters
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveRecipeFilterParameters(recipeFilterParameters)
    }

    fun applyQueries(recipeFilterParameters: DataStoreRepository.RecipeFilterParameters): HashMap<String, String> {
        val queries = HashMap<String, String>()
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_TYPE] =
            recipeFilterParameters.selectedMealType.takeIf { !it.equals(Constants.NO_FILTER, true) }
                ?: ""
        queries[Constants.QUERY_DIET] =
            recipeFilterParameters.selectedDietType.takeIf { !it.equals(Constants.NO_FILTER, true) }
                ?: ""
        queries[QUERY_CUISINE] =
            recipeFilterParameters.selectedCuisineType.takeIf {
                !it.equals(
                    Constants.NO_FILTER,
                    true
                )
            } ?: ""
        queries[QUERY_INTOLERANCES] =
            recipeFilterParameters.selectedIntoleranceType.takeIf {
                !it.equals(
                    Constants.NO_FILTER,
                    true
                )
            } ?: ""
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_SORT] = DEFAULT_SORT
        queries[QUERY_SORT_DIRECTIONS] = DEFAULT_SORT_DIRECTIONS
        return queries
    }
}