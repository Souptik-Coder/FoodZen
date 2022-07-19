package com.example.foody.ui.screens.recipeDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.models.AnalyzedInstructionItem
import com.example.foody.models.Recipe
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases
) : ViewModel() {
    init {
        Log.e("DetailsFragmentVM", "Instance created")
    }

    val analyzedInstructionResponse =
        MutableLiveData<NetworkResults<AnalyzedInstructionItem>>()

    fun getAnalyzedInstructions(id: Int) = viewModelScope.launch {
        analyzedInstructionResponse.value = NetworkResults.Loading()
        analyzedInstructionResponse.value = recipeUseCases.getAnalyzedInstruction(id)
    }

    fun insertOrUpdateRecentlyVisitedRecipe(recipe: Recipe) = viewModelScope.launch {
        recipeUseCases.insertRecentlyVisitedRecipe(recipe)
    }
}