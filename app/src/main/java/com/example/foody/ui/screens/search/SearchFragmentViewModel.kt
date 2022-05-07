package com.example.foody.ui.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.models.Recipe
import com.example.foody.models.RecipeSuggestionItem
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases
) : ViewModel() {
    val recipeSuggestionResponse = MutableLiveData<NetworkResults<List<RecipeSuggestionItem>>>()
    val recipeResponse = MutableLiveData<NetworkResults<List<Recipe>>>()
    private var recipeSuggestionJob: Job? = null

    fun getRecipeSuggestions(query: String) {
        recipeSuggestionJob?.cancel()
        recipeSuggestionJob = viewModelScope.launch {
            delay(500)
            recipeSuggestionResponse.value = NetworkResults.Loading()
            recipeSuggestionResponse.value = recipeUseCases.getRecipeSuggestion(query)
        }
    }

    fun getRecipeById(id: Int) = viewModelScope.launch {
        recipeResponse.value = NetworkResults.Loading()
        recipeResponse.value = recipeUseCases.getRecipeById(id)
    }
}