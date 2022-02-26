package com.example.foody.use_cases

import android.util.Log
import com.example.foody.R
import com.example.foody.data.repositories.Repository
import com.example.foody.models.RecipeListItem
import com.example.foody.models.toRecipeListItem
import com.example.foody.util.NetworkResults
import java.io.IOException

class GetRecipesByIngredient(
    private val repository: Repository
) {
    suspend operator fun invoke(queries: Map<String, String>): NetworkResults<List<RecipeListItem>> {
        return try {
            val response = repository.remote.getRecipeByIngredient(queries)
            if (response.isSuccessful) {
                val recipeByIngredientItems = response.body()!!
                val recipeListItems = ArrayList<RecipeListItem>()
                recipeByIngredientItems.forEach {
                    recipeListItems.add(it.toRecipeListItem())
                }
                Log.e("GRBI", "S=${recipeByIngredientItems.size}")
                NetworkResults.Success(recipeListItems)
            } else {
                NetworkResults.Error(R.string.unknown_error)
            }
        } catch (e: IOException) {
            NetworkResults.Error(R.string.internet_error)
        } catch (e: Exception) {
            NetworkResults.Error(R.string.unknown_error)
        }
    }
}