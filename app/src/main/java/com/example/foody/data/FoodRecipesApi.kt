package com.example.foody.data

import com.example.foody.models.*
import com.example.foody.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface FoodRecipesApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<RecipeList>

    @GET("recipes/{id}/information")
    suspend fun getRecipesById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<Recipe>

    @GET("recipes/{id}/card")
    suspend fun getRecipeCard(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<RecipeCard>

    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @QueryMap queries: Map<String, String>,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<List<RecipeByIngredientItem>>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("food/detect")
    suspend fun detectFoodInText(
        @Query("text") text: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<DetectedFood>

    @GET("food/ingredients/autocomplete")
    suspend fun ingredientAutoComplete(
        @Query("query") query: String,
        @Query("number") number: Int = 5,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<List<IngredientAutoCompleteItem>>

}