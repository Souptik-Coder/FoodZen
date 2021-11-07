package com.example.foody.data

import com.example.foody.models.RecipeList
import com.example.foody.models.Recipe
import com.example.foody.models.RecipeCard
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<RecipeList>

    @GET("recipes/{id}/information")
    suspend fun getRecipesById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey:String
    ): Response<Recipe>

    @GET("recipes/{id}/card")
    suspend fun getRecipeCard(
        @Path("id") id:Int,
        @Query("apiKey") apiKey:String
    ):Response<RecipeCard>
}