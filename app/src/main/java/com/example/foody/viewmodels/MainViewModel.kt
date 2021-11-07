package com.example.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.MyApplication
import com.example.foody.R
import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe
import com.example.foody.models.RecipeList
import com.example.foody.util.Constants.API_KEY
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
) : AndroidViewModel(application) {
    init {
        getFavouriteRecipes()
    }

    private val context by lazy { getApplication<MyApplication>().applicationContext }
    private val _recipeResponse: MutableLiveData<NetworkResults<RecipeList>> = MutableLiveData()
    val recipeResponse: LiveData<NetworkResults<RecipeList>> = _recipeResponse
    val favouriteRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val recentRecipes: MutableLiveData<List<Recipe>> = MutableLiveData(emptyList())


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        _recipeResponse.value = NetworkResults.Loading()
        try {
            val response = repository.remote.getRecipes(queries)
            when {
                response.isSuccessful -> {
                    _recipeResponse.value = handleFoodRecipeResponse(response)
                }
                //API Limit reached
                response.code() == 402 -> {
                    _recipeResponse.value =
                        NetworkResults.InternetError(
                            message =
                            context.getString(R.string.server_error_occurred)
                        )
                }
                else -> {
                    _recipeResponse.value =
                        NetworkResults.Error(message = response.errorBody()?.string())
                }
            }
        } catch (e: Exception) {
            _recipeResponse.value =
                NetworkResults.InternetError(
                    message =
                    context.getString(R.string.no_internet_showing_cached_data)
                )
        }
    }

    private fun handleFoodRecipeResponse(response: Response<RecipeList>):
            NetworkResults<RecipeList> {
        return when {
            response.body()?.recipeList.isNullOrEmpty() -> {
                insertAllRecentRecipes(RecipeList(emptyList()))
                NetworkResults.Error(
                    message = context.getString(R.string.no_recipe_found),
                    data = RecipeList(
                        emptyList()
                    )
                )
            }

            else -> {
                insertAllRecentRecipes(response.body()!!)
                NetworkResults.Success(response.body()!!)
            }
        }
    }

    fun getRecipeById(id: Int) = viewModelScope.launch {
        try {
            _recipeResponse.value = NetworkResults.Loading()
            val response = repository.remote.getRecipesById(id, API_KEY)
            if (response.isSuccessful) {
                val foodRecipe = RecipeList(listOf(response.body()!!))
                _recipeResponse.value = handleFoodRecipeResponse(Response.success(foodRecipe))
            } else
                _recipeResponse.value =
                    NetworkResults.Error(message = response.errorBody()?.string())
        } catch (exception: Exception) {
            _recipeResponse.value =
                NetworkResults.InternetError(message = context.getString(R.string.no_internet_connections))
        }
    }

    private fun getFavouriteRecipes() = viewModelScope.launch {
        repository.local.getAllFavouriteRecipes().collect {
            favouriteRecipes.value = it
        }
    }

    fun insertFavouriteRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.local.insertFavouriteRecipes(recipe)
    }

    fun deleteFavouriteRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.local.deleteFavouriteRecipe(recipe)
    }

    private fun insertAllRecentRecipes(recipeList: RecipeList) = viewModelScope.launch {
        repository.local.insertAllRecentRecipes(recipeList)
    }

    fun getAllRecentRecipes() = viewModelScope.launch {
        repository.local.getAllRecentRecipes().collect {
            recentRecipes.value = it
        }
    }
}