package com.example.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.data.Repository
import com.example.foody.models.FoodRecipes
import com.example.foody.util.Constants
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _recipeResponse: MutableLiveData<NetworkResults<FoodRecipes>> = MutableLiveData()
    val recipeResponse: LiveData<NetworkResults<FoodRecipes>> = _recipeResponse

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        try {
            _recipeResponse.value = NetworkResults.Loading()
            val response = repository.remote.getRecipes(queries)
            if (response.isSuccessful) {
                _recipeResponse.value = handleFoodRecipeResponse(response)
            } else {
                _recipeResponse.value =
                    NetworkResults.Error(message = response.errorBody()?.string())
            }
        } catch (e: Exception) {
            _recipeResponse.value = NetworkResults.Error(message = "No Internet Connection")
        }
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipes>):
            NetworkResults<FoodRecipes> {
        return when {
            response.body()?.results.isNullOrEmpty() ->
                NetworkResults.Error(
                    message = "No recipe found with the given filters\nTry changing some filters and try again",
                    data = FoodRecipes(
                        emptyList()
                    )
                )

            else ->
                NetworkResults.Success(response.body()!!)
        }
    }

    fun getRecipeById(id: Int) = viewModelScope.launch {
        try {
            _recipeResponse.value = NetworkResults.Loading()
            val response = repository.remote.getRecipesById(id, Constants.API_KEY)
            if (response.isSuccessful) {
                val foodRecipe = FoodRecipes(listOf(response.body()!!))
                _recipeResponse.value = handleFoodRecipeResponse(Response.success(foodRecipe))
            } else
                _recipeResponse.value =
                    NetworkResults.Error(message = response.errorBody()?.string())
        } catch (exception: Exception) {
            _recipeResponse.value = NetworkResults.Error(message = "No Internet Connection")
        }
    }
}