package com.example.foody.data

import android.util.Log
import com.example.foody.models.*
import com.example.foody.util.Constants.API_KEY
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

    suspend fun getRecipesById(id: Int, apiKey: String = API_KEY): Response<Recipe> {
        return foodRecipesApi.getRecipesById(id, apiKey)
    }

    suspend fun getRecipeCard(id: Int, apiKey: String = API_KEY): Response<RecipeCard> {
        return foodRecipesApi.getRecipeCard(id, apiKey)
    }

    suspend fun getRecipeByIngredient(queries: Map<String, String>): Response<List<RecipeByIngredientItem>> {
        Log.e("Queries", Gson().toJson(queries).toString())
        return foodRecipesApi.getRecipesByIngredients(queries)
    }

    suspend fun detectFoodInText(text: String): Response<DetectedFoodEntities> {
        Log.e("Queries", text)
        return foodRecipesApi.detectFoodInText(text)
    }

    suspend fun getIngredientSuggestion(query: String): Response<List<IngredientAutoCompleteItem>> {
        Log.e("Queries", query)
        return foodRecipesApi.ingredientAutoComplete(query)
    }

    suspend fun getRecipeSuggestions(query:String):Response<List<RecipeSuggestionItem>>{
        Log.e("Queries",query)
        return foodRecipesApi.recipeAutoComplete(query)
    }

    suspend fun getAnalyzedInstruction(id: Int):Response<List<AnalyzedInstructionItem>>{
        return foodRecipesApi.getAnalyzedInstruction(id)
    }

}