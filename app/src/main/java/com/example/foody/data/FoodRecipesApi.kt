package com.example.foody.data

import com.example.foody.models.FoodRecipes
import com.example.foody.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipes>

    @GET("recipes/{id}/information")
    suspend fun getRecipesById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey:String
    ): Response<Result>
}