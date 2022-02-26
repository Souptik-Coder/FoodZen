package com.example.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.MyApplication
import com.example.foody.R
import com.example.foody.data.repositories.Repository
import com.example.foody.models.RecipeCard
import com.example.foody.util.Constants
import com.example.foody.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeCardViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
) :
    AndroidViewModel(application) {

    private val context by lazy { getApplication<MyApplication>().applicationContext }
    val recipeCardResponse: MutableLiveData<NetworkResults<RecipeCard>> = MutableLiveData()

    fun getRecipeCard(id: Int) = viewModelScope.launch {
        try {
            recipeCardResponse.value = NetworkResults.Loading()
            val response = repository.remote.getRecipeCard(id, Constants.API_KEY)
            if (response.isSuccessful) {
                handleRecipeCardResponse(response)
            } else
                recipeCardResponse.value =
                    NetworkResults.Error(R.string.unknown_error)
        } catch (exception: Exception) {
            recipeCardResponse.value =
                NetworkResults.Error(R.string.internet_error)
        }
    }

    private fun handleRecipeCardResponse(response: Response<RecipeCard>) {
        if (response.code() == 404) {
            recipeCardResponse.value =
                NetworkResults.Error(R.string.recipe_card_not_found)
        } else
            recipeCardResponse.value = NetworkResults.Success(response.body()!!)
    }
}