package com.example.foody.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.models.Ingredient
import com.example.foody.models.RecipeListItem
import com.example.foody.use_cases.RecipeUseCases
import com.example.foody.util.Constants.DEFAULT_RECIPES_NUMBER
import com.example.foody.util.Constants.QUERY_NUMBER
import com.example.foody.util.NetworkResults
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchByIngredientFragmentViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases
) : ViewModel() {
    private val TAG = "SearchByIngredientVM"
    val detectedIngredientResponse = MutableLiveData<NetworkResults<List<Ingredient>>>()
    private val _recipeResponse: MutableLiveData<NetworkResults<List<RecipeListItem>>> =
        MutableLiveData()
    val recipeResponse: LiveData<NetworkResults<List<RecipeListItem>>> = _recipeResponse
    val ingredientSuggestionResponse = MutableStateFlow<NetworkResults<List<Ingredient>>>(
        NetworkResults.Success(
            emptyList()
        )
    )
    val currentIngredient = MutableLiveData<List<Ingredient>>(ArrayList())

    fun getRecipesByIngredient(queries: Map<String, String>) = viewModelScope.launch {
        _recipeResponse.value = NetworkResults.Loading()
        _recipeResponse.value = recipeUseCases.getRecipesByIngredient(queries)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries = HashMap<String, String>()
        var ingredients = ""
        currentIngredient.value?.forEach {
            ingredients += it.name + ","
        }
        queries["ingredients"] = ingredients
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries["ranking"] = "1"
        queries["ignorePantry"] = "true"
        return queries
    }

    private fun detectIngredientInText(text: String) = viewModelScope.launch {
        detectedIngredientResponse.value = NetworkResults.Loading()
        detectedIngredientResponse.value = recipeUseCases.detectFoodInText(text)
    }

    fun getIngredientSuggestion(query: String) = viewModelScope.launch {
        ingredientSuggestionResponse.value=NetworkResults.Loading()
        ingredientSuggestionResponse.value = recipeUseCases.getIngredientSuggestion(query)
    }

    fun recognizeTextFromImage(bitmap: Bitmap) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(image)
        result.addOnSuccessListener { visionText ->
            Log.e(TAG, "success called")
            detectIngredientInText(visionText.text)
        }
            .addOnFailureListener { e ->
                Log.e(TAG, e.message.orEmpty())
            }
    }

    fun setCurrentIngredients(ingredients: List<Ingredient>) {
        currentIngredient.value = ingredients
    }

    fun addIngredientToList(ingredient: Ingredient) {
        val currentList = ArrayList(currentIngredient.value!!)
        currentList.add(0, ingredient)
        currentIngredient.value = currentList
    }

    fun removeIngredientFromList(position: Int) {
        val currentList = ArrayList(currentIngredient.value!!)
        currentList.removeAt(position)
        currentIngredient.value = currentList
    }

}