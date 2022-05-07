package com.example.foody.ui.screens.analyzedInstructions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.models.AnalyzedInstructionItem
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyzedInstructionViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases
) : ViewModel() {
    val analyzedInstructionResponse =
        MutableLiveData<NetworkResults<List<AnalyzedInstructionItem>>>()

    fun getAnalyzedInstructions(id: Int) = viewModelScope.launch {
        analyzedInstructionResponse.value = NetworkResults.Loading()
        analyzedInstructionResponse.value = recipeUseCases.getAnalyzedInstruction(id)
    }
}