package com.example.foody.data

import android.util.Log
import com.example.foody.models.RecipeList
import com.example.foody.models.Recipe
import com.example.foody.models.RecipeCard
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<RecipeList> {
        Log.e("Queries", Gson().toJson(queries).toString())
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun getRecipesById(id: Int, apiKey: String): Response<Recipe> {
        return foodRecipesApi.getRecipesById(id, apiKey)
    }
    suspend fun getRecipeCard(id: Int, apiKey: String): Response<RecipeCard> {
        return foodRecipesApi.getRecipeCard(id,apiKey)
    }

}