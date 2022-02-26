package com.example.foody.use_cases

import com.example.foody.R
import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe
import com.example.foody.util.NetworkResults
import java.io.IOException

class GetRecipes(
    private val repository: Repository
) {
    suspend operator fun invoke(queries: Map<String, String>): NetworkResults<List<Recipe>> {
        return try {
            val response = repository.remote.getRecipes(queries)
            if (response.isSuccessful) {
                NetworkResults.Success(response.body()!!.recipeList)
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