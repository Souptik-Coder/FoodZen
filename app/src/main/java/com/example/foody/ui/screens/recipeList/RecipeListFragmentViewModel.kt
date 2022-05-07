package com.example.foody.ui.screens.recipeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.models.Recipe
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListFragmentViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases,
) : ViewModel() {
    private val _recipeResponse: MutableLiveData<NetworkResults<List<Recipe>>> = MutableLiveData()
    val recipeResponse: LiveData<NetworkResults<List<Recipe>>> = _recipeResponse

    fun getRecipeById(id: Int) = viewModelScope.launch {
        _recipeResponse.value = NetworkResults.Loading()
        _recipeResponse.value = recipeUseCases.getRecipeById(id)
    }
}