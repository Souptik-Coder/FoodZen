package com.example.foody.data

import android.util.Log
import com.example.foody.models.FoodRecipes
import com.example.foody.models.Result
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipes> {
        Log.d("Queries", Gson().toJson(queries).toString())
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun getRecipesById(id: Int, apiKey: String): Response<Result> {
        return foodRecipesApi.getRecipesById(id, apiKey)
    }
}